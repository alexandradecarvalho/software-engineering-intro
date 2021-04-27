package ies.lab3.datatopresentation;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "issues")
public class IssueReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String url;
    private String description;
    private boolean marked_as_private;
    private boolean updates;
    private boolean done;
    private Date created;
    private Date updated;

    public IssueReport(){}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMarked_as_private(boolean marked_as_private) {
        this.marked_as_private = marked_as_private;
    }

    public void setUpdates(boolean updates) {
        this.updates = updates;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMarked_as_private() {
        return marked_as_private;
    }

    public boolean isUpdates() {
        return updates;
    }

    public boolean isDone() {
        return done;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }
}
