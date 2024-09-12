# Connecting Spring Boot with DataGrid Operator

## Version
- Datagrid Operator 8.4.16
- Java 17
- Spring Boot 3.0.4

## Endpoints

### Populate data
```
$ curl -kv https://app:8080/populate
```

### Get in-memory cache data
```
$ curl -kv https://app:8080/cache-without-persistence
```

### Get persistence cache data
```
$ curl -kv https://app:8080/cache-with-persistence
```