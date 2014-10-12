package com.chongwu.config;

public class AuthParameters implements java.io.Serializable, Comparable<AuthParameters> {
        /**
         *  
         */
        private static final long serialVersionUID = -9041723304674844461L;
        private String name;
        private String value;

        public AuthParameters(String name, String value) {
                this.name = name;
                this.value = value;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getValue() {
                return value;
        }

        public void setValue(String value) {
                this.value = value;
        }

        @Override
        public int compareTo(AuthParameters param) {
                int compared;
                compared = this.name.compareTo(param.getName());
                if (0 == compared) {
                        compared = this.value.compareTo(param.getValue());
                }
                return compared;
        }
}
