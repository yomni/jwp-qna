package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        if (!this.answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public List<Answer> answers() {
        return Collections.unmodifiableList(answers);
    }
}
