package org.exoplatform.cs;
 
/**
 * this class will be used to display a contact information.
 * In our example, it will be created when the contactInfo event will be received
 * @author
 *
 */
public class ContactInfoBean {
    private String name = null;
    private String email = null;
 
    public ContactInfoBean() {
        super();
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
}