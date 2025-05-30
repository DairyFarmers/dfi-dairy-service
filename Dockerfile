FROM openjdk:17-alpine

# Install PostgreSQL and related tools
RUN apk update && apk add --no-cache postgresql postgresql-contrib su-exec


# Create app directory and copy JAR
COPY /target/dairy-inventory-service-0.0.1-SNAPSHOT.jar /app/dairy-inventory-service-0.0.1-SNAPSHOT.jar

# Create PostgreSQL data directory
RUN mkdir -p /var/lib/postgresql/data && chown -R postgres:postgres /var/lib/postgresql

# Copy entrypoint and make executable
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Expose ports
EXPOSE 5434
EXPOSE 8087

# Use entrypoint
ENTRYPOINT ["/bin/sh", "/entrypoint.sh"]