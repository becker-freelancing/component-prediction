package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.junit.commons.equals.EqualsTest;

class DocumentIdTest implements EqualsTest<DocumentId> {

    @Override
    public DocumentId baseObject() {
        return new DocumentId("1234");
    }

    @Override
    public DocumentId unequalObject() {
        return new DocumentId("1235");
    }
}