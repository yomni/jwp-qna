# JPA
> 객체지향프로그래밍과 관계형 데이터베이스는 근본적으로 괴리가 존재한다.  
> JPA는 객체지향을 좀 더 객체지향답게 사용하기 위한 기술로서, 데이터베이스에 의존적이지 않은 객체설계가 가능하게 만들어 준다.

## 1단계 - 엔티티 매핑
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- `DDL`을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- `@DataJpaTest`를 사용하여 `학습 테스트`를 해 본다.

### Entity
- User
  - [x] User 엔티티 매핑
  - [x] UserRepository 저장 및 find
- Question
  - [x] Question 엔티티 매핑
  - [x] QuestionRepository 저장 및 find
- Answer
  - [x] Answer 엔티티 매핑
  - [x] AnswerRepository 저장 및 find
- DeleteHistory
  - [x] DeleteHistory 엔티티 매핑

### 1단계 피드백
- [x] @Embeddable 타입을 @MappedSuperclass로 정의

## 2단계 - 연관 관계 매핑
> 1단계에서 매핑된 객체들은 뭔가 이상(?)하다. 객체지향적인 설계라면, 객체의 `참조`를 사용해 객체 그래프를 `탐색`해야 하지만,  
> DBMS의 설계대로 `외래 키`를 가지고 있다.  
>   
> 각 객체의 역할에 맞게 객체 사이의 연관 관계를 매핑해야 한다.
> ```java
> // 테이블 설계대로 맞추었을 경우 객체 탐색
> Question question = findQuestionById(questionId);
> List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
> 
> // 객체지향답게 설계했을 경우 객체의 참조를 사용해 탐색
> Question question = findQuestionById(questionId);
> List<Answer> answers = question.getAnswers();
> ```
```sql
-- DDL 힌트
alter table answer
  add constraint fk_answer_to_question
    foreign key (question_id)
      references question

alter table answer
  add constraint fk_answer_writer
    foreign key (writer_id)
      references user

alter table delete_history
  add constraint fk_delete_history_to_user
    foreign key (deleted_by_id)
      references user

alter table question
  add constraint fk_question_writer
    foreign key (writer_id)
      references user
```
![](src/main/resources/연관관계매핑.jpeg)
- [x] User : Question = 1 : *
- [x] User : Answer = 1 : *
- [x] User : DeleteHistory = 1 : 1
- [x] Question : Answer = 1 : *

### 2단계 피드백
- [ ] FetchType.LAZY 에 고민해 볼 것
- [ ] 연관관계 편의 메서드 적용해 볼 것