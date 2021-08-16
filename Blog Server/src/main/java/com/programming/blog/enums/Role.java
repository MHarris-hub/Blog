package com.programming.blog.enums;

public enum Role {
    USER {
        @Override
        public String toString() {
            return "ROLE_USER";
        }
    }
}
