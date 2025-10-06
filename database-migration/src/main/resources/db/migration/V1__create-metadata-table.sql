CREATE TABLE apps (
    id UUID PRIMARY KEY,
    app_name VARCHAR(1024) NOT NULL
);

CREATE TABLE tags (
    id UUID PRIMARY KEY,
    tag VARCHAR(1024) NOT NULL
);

CREATE TABLE document_metadata (
    id UUID PRIMARY KEY,
    app_id UUID NOT NULL,
    in_app_action_path TEXT NOT NULL,
    action_title TEXT NOT NULL,
    action_description  TEXT NOT NULL,
    action_short_description TEXT NOT NULL,
    locale VARCHAR(2) NOT NULL,
    version INT NOT NULL,
    created_at VARCHAR(100) NOT NULL,

    CONSTRAINT fk_app_id FOREIGN KEY (app_id) REFERENCES apps(id)

);



CREATE TABLE document_tags (
    document_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    PRIMARY KEY (document_id, tag_id),
    CONSTRAINT fk_document_id FOREIGN KEY (document_id) REFERENCES document_metadata(id),
    CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id)
);