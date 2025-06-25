# Spring Data JPA Order Service

This repository contains source code examples to support my course Spring Data JPA and Hibernate Beginner to Guru

## Additional Resources

For more information about transactions in database systems and Spring Data JPA, please refer to the following documents in the `doc` folder:

- [Overview of DB Transactions](doc/OverviewOfDBTransactions.pdf): This document provides a comprehensive overview of database transactions.
- [Spring Data JPA Transactions](doc/SpringDataJPATransactions.pdf): This guide offers insights into how transactions work specifically with Spring Data JPA.


## Flyway

To enable Flyway in the MySQL profile, override the following properties when starting the application:
- `spring.flyway.enabled = true`
- `spring.docker.compose.file = compose-mysql.yaml`

This profile starts MySQL on port 3306 using the Docker Compose file `compose-mysql-.yaml`.

## Docker

Docker Compose file initially use the startup script located in `src/scripts`. These scripts create the database and users.

## Kubernetes

### Generate Config Map for mysql init script

When updating 'src/scripts/init-mysql-mysql.sql', apply the changes to the Kubernetes ConfigMap:
```bash
kubectl create configmap mysql-init-script --from-file=init.sql=src/scripts/init-mysql.sql --dry-run=client -o yaml | Out-File -Encoding utf8 k8s/mysql-init-script-configmap.yaml
```

### Deployment with Kubernetes

To deploy all resources:
```bash
kubectl apply -f k8s/
```

To remove all resources:
```bash
kubectl delete -f k8s/
```

Check
```bash
kubectl get deployments -o wide
kubectl get pods -o wide
```

### Deployment with Helm

Be aware that we are using a different namespace here (not default).

Go to the directory where the tgz file has been created after 'mvn install'
```powershell
cd target/helm/repo
```

unpack
```powershell
$file = Get-ChildItem -Filter sdjpa-order-service-v*.tgz | Select-Object -First 1
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

You can use the actuator rest call to verify via port 30080

## Running the Application
1. Choose between h2 or mysql for database schema management. (you can use one of the preconfigured intellij runners)
2. Start the application with the appropriate profile and properties.
3. The application will use Docker Compose to start MySQL and apply the database schema changes.