package bd.ohedulalam.polls.model;

public class ChoiceVoteCount {

    private long choiceId;
    private long voteCount;

    public ChoiceVoteCount(long choiceId, long voteCount) {
        this.choiceId = choiceId;
        this.voteCount = voteCount;
    }

    public long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(long choiceId) {
        this.choiceId = choiceId;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
}
