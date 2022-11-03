package subway.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

// Slicing Test ==> 원하는 레이어만 테스트
// DataJpaTest : Repository 레이어만 테스트
@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Autowired
    private TestEntityManager manager;

//    @Autowired
//    private EntityManager manager2;

    @Test

    void save() {
        final Station station = new Station(1L, "잠실역");
        // 주의 : 버릇처럼 save를 리턴받아 사용해라..!
        final Station actual = stations.save(station);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        stations.save(new Station("잠실역"));
        final Station actual = stations.findByName("잠실역");

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void identity() {
        // begin
        Station station1 = stations.save(new Station("잠실역"));
//        Station station2 = stations.findByName("잠실역");
         Station station2 = stations.findById(station1.getId()).get();

        // equals : 동등성 (값) 비교
        // == : 동일성(Instance) 비교
        assertThat(station1 == station2).isTrue();
        // commit
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        // 변경감지 ; Dirty Checking : snapshot을 비교하여 엔티티와 비교하여 변경점이 있으면 update query가 나간다
        station1.changeName("몽촌토성역");

        // Dirty Checking 시 변경점이 없으므로 update 쿼리가 안나감
//        station1.changeName("잠실역");

        // 강제 flush, clear
        manager.flush();
        manager.clear();

//        Station station2 = stations.findByName("몽촌토성역");
        Station station2 = stations.findById(station1.getId()).get();
//        stations.flush();

        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLine() {
        Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        station = stations.save(station);
        flushAndClear();
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine()).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        Line line = lines.save(new Line("2호선"));
        Station station = stations.findByName("교대역");
        station.setLine(line);
        flushAndClear();
    }

    @Test
    void removeLine() {
        // 연관관계 끊기
        Station station = stations.findByName("교대역");
        station.setLine(null);
        flushAndClear();
    }

    @Test
    void deleteLine() {
        Line line = lines.findById(1L).get();
        lines.delete(line);
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}