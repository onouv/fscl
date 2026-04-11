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

The manifest set also includes a single-node NATS JetStream broker in the `fscl` namespace for backend-to-backend messaging. It is modeled as a StatefulSet with a persistent volume claim so streams survive pod restarts on the same cluster storage. The `process-api` deployment in the `fscl-process` namespace is wired to it as an event consumer, and the included NetworkPolicy resources restrict broker and PostgreSQL ingress to the expected callers.

The current JetStream convention for the process bounded context is:

- Stream: `fscl-events`
- Subjects: `fscl.process.*`
- Durable consumer: `process-api`
- Ack policy: explicit acknowledgements
- Ack wait: `30s`
- Retention mode: `limits`

Shared messaging runtime values for the bounded context are split into `15-process-messaging.yaml`, while `20-process-api.yaml` keeps only process-api-specific consumer settings. Code-level contract constants such as the outbox notify channel remain in `fscl-messaging` and are not repeated as deployment configuration. The manifest set does not create JetStream streams or consumers automatically; it defines the expected naming and delivery semantics for the application to apply when it connects.

The manifest set now also includes a YAML-only placeholder in `27-jetstream-bootstrap.yaml` for future JetStream provisioning. It is a suspended `Job` plus a `ConfigMap` that carry the intended stream values, but it intentionally does not include runnable bootstrap logic yet.

The durable consumer matters because `process-api` gives the process backend a stable acknowledgement position in JetStream. When the backend restarts, JetStream can continue from the last acknowledged message for that consumer instead of treating the backend as a brand new ephemeral reader.

The manifest set also includes YAML scaffolding for an `outbox-publisher` sidecar in the `process-api` pod. This follows the transactional outbox shape: the application writes domain changes and outbox rows into PostgreSQL, then the sidecar reads pending outbox rows and publishes them to JetStream on `fscl.process.*`. Keeping the publisher as a sidecar is reasonable here because it shares the same bounded-context database and deployment lifecycle as the process backend while still separating publishing concerns from the main API container.

The sidecar scaffold is split into `15-process-messaging.yaml` for shared messaging config, `22-outbox-publisher.yaml` for publisher-local runtime config, and `20-process-api.yaml` for the actual sidecar container wiring. It is intentionally only deployment scaffolding at this point: placeholder image, environment contract, and network access, but no implementation code.

The outbox notify channel is treated as a shared messaging contract and therefore stays fixed at `fscl_outbox` in Rust code through `fscl-messaging` instead of being repeated as deployment configuration.

Migration ownership remains with the bounded-context service database. The database-owning service is responsible for applying both its own schema and the shared outbox schema; the sidecar publisher only consumes rows from that schema.

The NATS ingress policy is keyed off the pod label `fscl.io/nats-client=true` instead of a single workload name. That makes it easier to add more backend pods that need broker access later without changing the broker policy shape each time. Because the publisher is a sidecar in the same pod, the label covers both the process-api consumer and the publisher container.

The PostgreSQL manifest is tuned for a local cluster with health probes and persistent storage, but it is still only a single-instance database. The JetStream broker is also only a single node, which is enough for local development but not enough for highly available messaging. For production-grade PostgreSQL or NATS JetStream, add backup tooling and the operator/controller model you intend to support operationally.