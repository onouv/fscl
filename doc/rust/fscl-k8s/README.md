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

The manifest set also includes YAML scaffolding for an `outbox-publisher` sidecar in the `process-api` pod. This follows the transactional outbox shape: the application writes domain changes and outbox rows into PostgreSQL, then the sidecar reads pending outbox rows and publishes them to JetStream on `fscl.process.*`. Keeping the publisher as a sidecar is reasonable here because it shares the same bounded-context database and deployment lifecycle as the process backend while still separating publishing concerns from the main API container.

The sidecar scaffold is split into `22-outbox-publisher.yaml` for its configuration and `20-process-api.yaml` for the actual sidecar container wiring. It is intentionally only deployment scaffolding at this point: placeholder image, environment contract, and network access, but no implementation code.

The NATS ingress policy is keyed off the pod label `fscl.io/nats-client=true` instead of a single workload name. That makes it easier to add more backend pods that need broker access later without changing the broker policy shape each time.

The PostgreSQL manifest is tuned for a local cluster with health probes and persistent storage, but it is still only a single-instance database. The JetStream broker is also only a single node, which is enough for local development but not enough for highly available messaging. For production-grade PostgreSQL or NATS JetStream, add backup tooling and the operator/controller model you intend to support operationally.