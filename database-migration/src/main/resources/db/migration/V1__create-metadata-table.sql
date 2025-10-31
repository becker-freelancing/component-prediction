CREATE TABLE apps (
    id UUID PRIMARY KEY,
    app_name VARCHAR(1024) NOT NULL
);

CREATE TABLE tags (
    id UUID PRIMARY KEY,
    tag VARCHAR(1024) NOT NULL
);

CREATE TABLE source_metadata (
    id UUID PRIMARY KEY,
    app_id UUID NOT NULL,
    locale VARCHAR(2) NOT NULL,
    version INT NOT NULL,
    created_at VARCHAR(100) NOT NULL,
    last_modified_at VARCHAR(100) NOT NULL,
    file_name VARCHAR(512),
    parent_id UUID,

    CONSTRAINT fk_app_id FOREIGN KEY (app_id) REFERENCES apps(id),
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES source_metadata(id)
);



CREATE TABLE metadata_tags (
    metadata_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    PRIMARY KEY (metadata_id, tag_id),
    CONSTRAINT fk_metadata_id FOREIGN KEY (metadata_id) REFERENCES source_metadata(id),
    CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id)
);