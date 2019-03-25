package bd.ohedulalam.polls.payload;

public class UserIdentityAvaivality {
    private boolean available;

    public UserIdentityAvaivality(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
