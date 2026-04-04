# FSCL Kubernetes Example

This manifest set is split into small modules and can be applied with Kustomize:

```sh
kubectl apply -k doc/rust/fscl-k8s
```

For OpenShift Local, the Gateway host uses the default wildcard apps domain:

```text
fscl.apps-crc.testing
```

The Gateway class is controller-specific. Discover it with:

```sh
oc get gatewayclass
```

Then replace `TODO-SET-GATEWAYCLASS` in `40-openshift-local-gateway.yaml` with one of the returned names.

If the command returns no GatewayClass resources, the cluster does not currently have a Gateway controller installed. In that case, either install one or switch this exposure layer to OpenShift `Route` resources.

The manifest set also includes a single-node NATS JetStream broker in the `fscl` namespace for backend-to-backend messaging. It is modeled as a StatefulSet with a persistent volume claim so streams survive pod restarts on the same cluster storage. The `fscl-process` API is wired to it through environment variables, and the included NetworkPolicy resources restrict broker and PostgreSQL ingress to the expected callers.

The current JetStream convention for the process backend is:

- Stream: `fscl-events`
- Subjects: `fscl.process.*`
- Durable consumer: `process-worker`
- Ack policy: explicit acknowledgements
- Ack wait: `30s`
- Retention mode: `limits`

These values are exposed in `20-process-api.yaml` so the application contract is visible in the same place as the deployment configuration. The manifest set does not create JetStream streams or consumers automatically; it defines the expected naming and delivery semantics for the application to apply when it connects.

The manifest set now also includes a YAML-only placeholder in `27-jetstream-bootstrap.yaml` for future JetStream provisioning. It is a suspended `Job` plus a `ConfigMap` that carry the intended stream and durable consumer values, but it intentionally does not include runnable bootstrap logic yet.

The durable consumer matters because `process-worker` gives the process backend a stable acknowledgement position in JetStream. When the backend restarts, JetStream can continue from the last acknowledged message for that consumer instead of treating the backend as a brand new ephemeral reader.

The PostgreSQL manifest is tuned for a local cluster with health probes and persistent storage, but it is still only a single-instance database. The JetStream broker is also only a single node, which is enough for local development but not enough for highly available messaging. For production-grade PostgreSQL or NATS JetStream, add backup tooling and the operator/controller model you intend to support operationally.