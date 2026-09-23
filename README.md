# Spring Data JPA Order Service

Spring Boot 4 / Spring Data JPA capstone project on Java 25, implementing an order service with
customers, products, categories, orders, order lines and order approvals. It demonstrates JPA
relationship mappings (1:N, bidirectional 1:1, M:N via join table), embedded types, optimistic
locking and entity timestamps against H2 (MySQL-compat mode) or MySQL with Flyway schema
management, and ships as a Docker/Helm deployment to Kubernetes with W3C tracing and contextual
logging. Source code examples to support my course Spring Data JPA and Hibernate Beginner to Guru.

## Architecture Overview

```mermaid
graph LR
    Client(["Client"])

    subgraph App ["Spring Boot App :8080 (k8s :30080)"]
        Bootstrap["BootstrapOrderService\n(TestDataLoader, seed data)"]
        Service["ProductService\nProductServiceImpl"]
        Repos["Spring Data JPA\nRepositories"]
        Observability["RequestLoggingConfig\nW3C tracing, MDC"]
    end

    subgraph Domain ["Capstone Domain Model"]
        Orders["OrderHeader / OrderLine /\nOrderApproval"]
        Party["Customer\n(Address embedded)"]
        Catalog["Product / Category"]
    end

    subgraph Migration ["Schema Management"]
        Flyway["Flyway\ndb/migration V2.0-V15.0"]
    end

    subgraph Databases ["Databases"]
        H2[("H2\nIn-Memory (h2 profile)")]
        MySQL[("MySQL\nDocker Compose (mysql profile)")]
    end

    Client -->|"actuator"| App
    Bootstrap --> Service
    Bootstrap --> Repos
    Service --> Repos
    Repos --> Domain
    Repos <--> H2
    Repos <--> MySQL
    Flyway --> MySQL
```

## Database Schema

```mermaid
erDiagram
    customer {
        BIGINT       id PK "auto_increment"
        INTEGER      version "optimistic lock"
        VARCHAR(50)  customer_name
        VARCHAR(30)  address "embedded"
        VARCHAR(30)  city "embedded"
        VARCHAR(30)  state "embedded"
        VARCHAR(30)  zip_code "embedded"
        VARCHAR(20)  phone
        VARCHAR(255) email
    }

    order_header {
        BIGINT      id PK "auto_increment"
        INTEGER     version "optimistic lock"
        BIGINT      customer_id FK
        VARCHAR(30) order_status "enum: NEW, IN_PROCESS, COMPLETE"
        VARCHAR(30) shipping_address "embedded"
        VARCHAR(30) shipping_city "embedded"
        VARCHAR(30) shipping_state "embedded"
        VARCHAR(30) shipping_zip_code "embedded"
        VARCHAR(30) bill_to_address "embedded"
        VARCHAR(30) bill_to_city "embedded"
        VARCHAR(30) bill_to_state "embedded"
        VARCHAR(30) bill_to_zip_code "embedded"
        BIGINT      order_approval_id FK
    }

    order_line {
        BIGINT  id PK "auto_increment"
        INTEGER version "optimistic lock"
        INTEGER quantity_ordered
        BIGINT  order_header_id FK
        BIGINT  product_id FK
    }

    product {
        BIGINT       id PK "auto_increment"
        INTEGER      version "optimistic lock"
        VARCHAR(255) description
        VARCHAR(30)  product_status "enum: NEW, IN_STOCK, DISCONTINUED"
        INTEGER      quantity_on_hand
    }

    category {
        BIGINT       id PK "auto_increment"
        INTEGER      version "optimistic lock"
        VARCHAR(255) description
    }

    order_approval {
        BIGINT       id PK "auto_increment"
        INTEGER      version "optimistic lock"
        VARCHAR(255) approved_by
        BIGINT       order_header_id FK
    }

    customer     ||--o{ order_header   : "customer_id"
    order_header ||--o{ order_line     : "order_header_id"
    order_header ||--o| order_approval : "order_approval_id"
    product      ||--o{ order_line     : "product_id"
    product      }o--o{ category       : "product_category"
```

## Flyway

The MySQL profile enables Flyway out of the box — the following properties are already set in `application-mysql.yaml`:
- `spring.flyway.enabled = true`
- `spring.docker.compose.file = compose-mysql.yaml`

This profile starts MySQL on port 3306 using the Docker Compose file `compose-mysql.yaml`.

## Docker

Docker Compose file initially use the startup script located in `src/scripts`. These scripts create the database and users.

### Deployment with Helm

Be aware that we are using a different namespace here (not default).

Go to the directory where the tgz file has been created after 'mvn install'

```powershell
cd target/helm/repo
```

unpack

```powershell
$file = Get-ChildItem -Filter sdjpa-order-service-chart-*.tgz | Select-Object -First 1
tar -xvf $file.Name
```

install

```powershell
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name
helm upgrade --install $APPLICATION_NAME ./$APPLICATION_NAME --namespace sdjpa-order-service --create-namespace --wait --timeout 5m --debug
```

show logs

```powershell
kubectl get pods -n sdjpa-order-service
```

replace $POD with pods from the command above

```powershell
kubectl logs $POD -n sdjpa-order-service --all-containers
```

Show Endpoints

```powershell
kubectl get endpoints -n sdjpa-order-service
```

test

```powershell
helm test $APPLICATION_NAME --namespace sdjpa-order-service --logs
```

status

```powershell
helm status $APPLICATION_NAME --namespace sdjpa-order-service
```

uninstall

```powershell
helm uninstall $APPLICATION_NAME --namespace sdjpa-order-service
```

delete all

```powershell
kubectl delete all --all -n sdjpa-order-service
```

create busybox sidecar

```powershell
kubectl run busybox-test --rm -it --image=busybox:1.36 --namespace=sdjpa-order-service --command -- sh
```

You can use the actuator rest call to verify via port 30080

## Running the Application

1. Choose between h2 or mysql for database schema management. (you can use one of the preconfigured intellij runners)
2. Start the application with the appropriate profile and properties.
3. The application will use Docker Compose to start MySQL and apply the database schema changes.

## Sandbox (local dev environment)

The sandbox is provisioned by the opencode-sandbox-kit and runs as a Docker container. It mounts this
repo, starts the agent, and connects the IntelliJ MCP server. The app runs on port `8080`; `compose-mysql.yaml`
provides MySQL.

Allow the kit source (GitHub without cloning):

```powershell
sbx settings set kit.allowedSources --% "[\"docker.io/\",\"github.com/dboeckli/\"]
```

Start a new sandbox:

```powershell
sbx run opencode `
    --kit "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent" `
    --template docker/sandbox-templates:opencode-docker-0.5.0 `
    --skills=off `
    --static-mcp idea `
    . `
    "C:\development\maven-repo:ro"
```

Start the sandbox with Kubernetes support:

```powershell
sbx run opencode `
    --kit "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent" `
    --template docker/sandbox-templates:opencode-docker-0.5.0 `
    --skills=off `
    --static-mcp idea `
    . `
    "C:\development\maven-repo:ro" `
    "$env:USERPROFILE\.kube:ro"
```

Claude Code (Home) and Mammouth Code variants:

```powershell
sbx run claude `
    --kit "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=opencode-agent" `
    --template docker/sandbox-templates:claude-code-docker-0.5.0 `
    --skills=off `
    --static-mcp idea `
    . `
    "C:\development\maven-repo:ro"
```

```powershell
sbx run "git+https://github.com/dboeckli/opencode-sandbox-kit.git#dir=mammouth-agent" `
    --skills=off `
    --static-mcp idea `
    . `
    "C:\development\maven-repo:ro"
```

### Start the app

Start MySQL (H2 needs no Docker):

```shell
docker compose -f compose-mysql.yaml up
```

Then run one of the IntelliJ run configurations (`.run/Spring6Application h2.run.xml` or the MySQL
one) or start via `./mvnw spring-boot:run -Dspring-boot.run.profiles=h2`.

### Sandbox build quirk

The sandbox mounts the repo via filesystem passthrough, which blocks symlinks — Spotless's `npm install`
(prettier) would fail with `EPERM` unless npm skips bin links. The kit sets `npm_config_bin_links=false`
globally, so no manual export is needed.
