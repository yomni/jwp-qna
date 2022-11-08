package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAndFind() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        flushAndClear();

        User javajigi = userRepository.findById(1L).get();
        User sanjigi = userRepository.findById(2L).get();
        assertAll(
                () -> assertThat(javajigi.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(sanjigi.getUserId()).isEqualTo("sanjigi")
        );
    }

    @Test
    void updateAndFind() {
        // TODO : save와 같이 통합테스트 하는 경우 update가 실패함
        userRepository.save(SANJIGI);
        userRepository.save(JAVAJIGI);
        flushAndClear();

        SANJIGI.update(SANJIGI, JAVAJIGI);
        userRepository.save(SANJIGI);
        userRepository.save(JAVAJIGI);
        flushAndClear();

        User sanjigi = userRepository.findByUserId(SANJIGI.getUserId()).get();
        assertThat(sanjigi.getEmail()).isEqualTo(JAVAJIGI.getEmail());
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
