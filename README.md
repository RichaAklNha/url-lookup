url-lookup

To setup postgres and redis (as docker images), run :
docker-compose -f docker-compose.yml up -d

assumed lookup_db meta-data:


           List of relations
 Schema |    Name    | Type  |  Owner
--------+------------+-------+----------
 public | pretty_url | table | postgres
 
 
                     Table "public.pretty_url"
 Column |          Type          | Collation | Nullable | Default
--------+------------------------+-----------+----------+---------
 path   | character varying(256) |           |  false   |
 pretty | character varying(256) |           |  false   |
 id     | integer                |           |  false   |
Indexes:
    "unique_path" UNIQUE CONSTRAINT, btree (path)
    "unique_pretty" UNIQUE CONSTRAINT, btree (pretty)

To run the application-server:
-> mvn clean install
-> mvn package
-> java -Dspring.datasource.url=jdbc:postgresql://<host>/lookup_db -Dspring.datasource.username=<postgres_user> -Dspring.datasource.password=<postgres_password> 
-Dredis.host=<redis_host> -Dredis.port=<redis_port> -jar target/url-lookup-0.0.1-SNAPSHOT.jar
(provide as environment properties postgres and redis server conifgurations when not using given default configurations in docker-compose)

