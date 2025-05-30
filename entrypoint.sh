#!/bin/sh

LOGFILE=/var/lib/postgresql/logfile
DATA_DIR=/var/lib/postgresql/data
PG_USER=
PG_PORT=
POSTGRES_DB=
POSTGRES_PASSWORD=

mkdir -p /run/postgresql
chown $PG_USER:$PG_USER /run/postgresql

if [ ! -s "$DATA_DIR/PG_VERSION" ]; then
  echo "Initializing PostgreSQL database..."
  su-exec $PG_USER initdb -D "$DATA_DIR"

  echo "Starting PostgreSQL temporarily for setup..."
  su-exec $PG_USER pg_ctl -D "$DATA_DIR" -o "-p $PG_PORT" -w start

  echo "Setting password for user $PG_USER ..."
  su-exec $PG_USER psql -p $PG_PORT -c "ALTER USER $PG_USER WITH PASSWORD '$POSTGRES_PASSWORD';"

  echo "Creating database $POSTGRES_DB ..."
  su-exec $PG_USER createdb -p $PG_PORT -O $PG_USER $POSTGRES_DB

  echo "Stopping temporary PostgreSQL server..."
  su-exec $PG_USER pg_ctl -D "$DATA_DIR" -m fast -w stop
fi

echo "Starting PostgreSQL on port $PG_PORT..."
su-exec $PG_USER pg_ctl -D "$DATA_DIR" -o "-p $PG_PORT" -l "$LOGFILE" start

echo "Waiting for PostgreSQL to be ready on port $PG_PORT..."
until su-exec $PG_USER pg_isready -p $PG_PORT; do
  sleep 1
done

# Run the Java application
echo "PostgreSQL is ready. Starting Java application..."
exec java -jar /app/dairy-inventory-service-0.0.1-SNAPSHOT.jar