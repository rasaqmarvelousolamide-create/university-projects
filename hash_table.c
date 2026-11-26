#include "hash_table.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Initialise every bucket so that the table starts empty. */
void initialise_hash_table(HashTable *ht)
{
    int i;

    if (ht == NULL) {
        return;
    }

    for (i = 0; i < HASH_TABLE_SIZE; i++) {
        ht->buckets[i] = NULL;
    }
}

/* Return the bucket index for a valid user ID, or -1 for invalid input. */
int get_bucket_for_user(int user_id)
{
    if (user_id < 1 || user_id > 2139) {
        return -1;
    }

    return user_id % HASH_TABLE_SIZE;
}

/* Search the selected bucket, following the linked list if necessary. */
User *find_user(const HashTable *ht, int user_id)
{
    int bucket;
    User *current;

    if (ht == NULL) {
        return NULL;
    }

    bucket = get_bucket_for_user(user_id);
    if (bucket == -1) {
        return NULL;
    }

    current = ht->buckets[bucket];
    while (current != NULL) {
        if (current->user_id == user_id) {
            return current;
        }
        current = current->next;
    }

    return NULL;
}

/* Add a user at the head of a bucket, or update an existing user's password. */
int register_user(HashTable *ht, int user_id, char password[])
{
    int bucket;
    size_t password_length;
    User *existing;
    User *new_user;

    if (ht == NULL || password == NULL) {
        return ERROR;
    }

    password_length = strlen(password);
    if (password_length > MAX_PASSWORD_LEN) {
        return ERROR;
    }

    bucket = get_bucket_for_user(user_id);
    if (bucket == -1) {
        return ERROR;
    }

    existing = find_user(ht, user_id);
    if (existing != NULL) {
        memcpy(existing->password, password, password_length + 1);
        return SUCCESS;
    }

    new_user = malloc(sizeof *new_user);
    if (new_user == NULL) {
        return ERROR;
    }

    new_user->user_id = user_id;
    memcpy(new_user->password, password, password_length + 1);
    new_user->next = ht->buckets[bucket];
    ht->buckets[bucket] = new_user;

    return SUCCESS;
}

/* Check whether the supplied password matches the stored password. */
int is_valid_password(const HashTable *ht, int user_id, char password[])
{
    User *user;

    if (ht == NULL || password == NULL) {
        return ERROR;
    }

    if (strlen(password) > MAX_PASSWORD_LEN) {
        return ERROR;
    }

    user = find_user(ht, user_id);
    if (user == NULL) {
        return ERROR;
    }

    if (strcmp(user->password, password) == 0) {
        return SUCCESS;
    }

    return ERROR;
}

/* Release every node in every bucket and reset each bucket to NULL. */
void free_buckets(HashTable *ht)
{
    int i;

    if (ht == NULL) {
        return;
    }

    for (i = 0; i < HASH_TABLE_SIZE; i++) {
        User *current = ht->buckets[i];

        while (current != NULL) {
            User *next_user = current->next;
            free(current);
            current = next_user;
        }

        ht->buckets[i] = NULL;
    }
}

/*
 * Load users from a text file into the hash table.
 * Expected format: one record per line, "user_id password".
 * Users are added in file order, so earlier lines are inserted first.
 * Returns SUCCESS if the whole file loads, otherwise ERROR.
 * On any error the table is emptied, so no partial data is left behind.
 */
int load_users_from_file(HashTable *ht, char filename[])
{
    FILE *file;
    char line[256];

    /* Basic checks: reject NULL pointers and an empty filename */
    if (ht == NULL || filename == NULL || filename[0] == '\0') {
        return ERROR;
    }

    file = fopen(filename, "r");
    if (file == NULL) {
        return ERROR;
    }

    /* Read the file one line at a time, preserving file order */
    while (fgets(line, sizeof line, file) != NULL) {
        int user_id;
        int fields;
        char password[MAX_PASSWORD_LEN + 1];
        char extra;
        size_t line_length = strlen(line);

        /* No newline and not at end of file means the line was too
           long for the buffer, so treat the file as invalid */
        if (line_length > 0 && line[line_length - 1] != '\n' && !feof(file)) {
            fclose(file);
            free_buckets(ht);
            return ERROR;
        }

        /* Skip blank lines, including lines with only whitespace */
        if (strspn(line, " \t\r\n") == line_length) {
            continue;
        }

        /* Expect exactly two fields: an ID and a password of at most
           40 characters. The extra %c catches a third field or a
           password longer than 40, since either makes sscanf return 3. */
        fields = sscanf(line, " %d %40s %c", &user_id, password, &extra);
        if (fields != 2) {
            fclose(file);
            free_buckets(ht);
            return ERROR;
        }

        /* register_user checks the ID range and adds to the correct
           bucket, handling chaining and duplicate IDs */
        if (register_user(ht, user_id, password) != SUCCESS) {
            fclose(file);
            free_buckets(ht);
            return ERROR;
        }
    }

    /* fgets also returns NULL on a read error, not just at end of file */
    if (ferror(file)) {
        fclose(file);
        free_buckets(ht);
        return ERROR;
    }

    /* Make sure the file closed cleanly before reporting success */
    if (fclose(file) != 0) {
        free_buckets(ht);
        return ERROR;
    }

    return SUCCESS;
}
