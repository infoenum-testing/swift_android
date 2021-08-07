package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEmailResponseModel {

    public class Element {

        @SerializedName("handle~")
        @Expose
        private Handle handle;

        public Handle getHandle() {
            return handle;
        }

        public void setHandle(Handle handle) {
            this.handle = handle;
        }

    }


        @SerializedName("elements")
        @Expose
        private List<Element> elements = null;

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }

    public class Handle {

        @SerializedName("emailAddress")
        @Expose
        private String emailAddress;

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

    }
}
