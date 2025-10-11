#!/bin/bash

# Demo Database Backup Script
# This script creates a complete backup of the "base" demo database

# Database configuration (from application.properties)
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="base"
DB_USER="user"
DB_PASSWORD="password"

# Backup configuration
BACKUP_DIR="./database_backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="demo_database_backup_${TIMESTAMP}.sql"
BACKUP_PATH="${BACKUP_DIR}/${BACKUP_FILE}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Demo Database Backup Script ===${NC}"
echo "Database: ${DB_NAME}"
echo "Host: ${DB_HOST}:${DB_PORT}"
echo "User: ${DB_USER}"
echo "Backup file: ${BACKUP_PATH}"
echo
# Ensure backup directory exists
mkdir -p "$BACKUP_DIR"

# ---- Perform the backup ----
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${DATE}.sql"

echo "Starting backup of database: $DB_NAME"
pg_dump -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -F c -b -v -f "$BACKUP_FILE" "$DB_NAME"

# ---- Compress the backup ----
gzip "$BACKUP_FILE"

# ---- Optional: Remove old backups (older than 7 days) ----
find "$BACKUP_DIR" -type f -name "*.gz" -mtime +7 -delete

echo "Backup completed: ${BACKUP_FILE}.gz"