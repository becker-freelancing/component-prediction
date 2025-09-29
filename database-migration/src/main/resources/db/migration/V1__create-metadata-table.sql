CREATE TABLE apps (
    id INT PRIMARY KEY,
    app_name VARCHAR(1024) NOT NULL
);

CREATE TABLE users (
    id INT PRIMARY KEY,
    username VARCHAR(16) UNIQUE NOT NULL
);

CREATE TABLE tags (
    id INT PRIMARY KEY,
    tag VARCHAR(1024) NOT NULL
);

CREATE TABLE document_metadata (
    id INT PRIMARY KEY,
    document_id UUID NOT NULL,
    app_id INT NOT NULL,
    in_app_action_path TEXT NOT NULL,
    action_title TEXT NOT NULL,
    action_description  TEXT NOT NULL,
    action_short_description TEXT NOT NULL,
    locale VARCHAR(2) NOT NULL,
    version INT NOT NULL,
    creator_id INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_app_id FOREIGN KEY (app_id) REFERENCES apps(id),
    CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES users(id)

);



CREATE TABLE document_tags (
    document_id INT NOT NULL,
    tag_id INT NOT NULL,

    PRIMARY KEY (document_id, tag_id),
    CONSTRAINT fk_document_id FOREIGN KEY (document_id) REFERENCES document_metadata(id),
    CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id)
);