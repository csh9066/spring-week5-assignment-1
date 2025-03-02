package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserSignupData;
import com.codesoom.assignment.dto.UserUpdateInfoData;
import com.codesoom.assignment.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    public static final String EMAIL = "kimchi@joa.com";
    public static final String NAME = "갓김치";
    public static final String PASSWORD = "1234567";

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("signup 메소드는")
    class Describe_signup {

        private final UserSignupData userSignupData = UserSignupData.builder()
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .build();

        @Test
        @DisplayName("생성된 user를 반환한다.")
        void it_returns_created_user() {
            User user = userService.signUp(userSignupData);

            assertThat(user.getId()).isNotNull();
            assertThat(user.getEmail()).isEqualTo(EMAIL);
            assertThat(user.getName()).isEqualTo(NAME);
            assertThat(user.getPassword()).isEqualTo(PASSWORD);
        }
    }

    @Nested
    @DisplayName("updateInfo 메소드는")
    class Describe_updateInfo {

        private final UserUpdateInfoData userUpdateInfoData =
                UserUpdateInfoData.builder()
                        .name("배추김치")
                        .password("1234")
                        .build();

        @DataJpaTest
        @Nested
        @DisplayName("존재하는 아이디가 주어지면")
        class Context_with_existed_id {
            private Long id;

            @BeforeEach
            void setUp() {
                User savedUser = userRepository.save(user);
                id = savedUser.getId();
            }

            @Test
            @DisplayName("변경된 user를 반환한다.")
            void it_returns_updated_user() {
                User user = userService.updateInfo(id, userUpdateInfoData);

                assertThat(user.getName()).isEqualTo("배추김치");
                assertThat(user.getPassword()).isEqualTo("1234");
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("존재하지 않는 아이디가 주어지면")
        class Context_with_not_existed_id {

            private final Long INVALID_ID = 100L;

            @Test
            @DisplayName("UserNotFoundException 예외를 던진다.")
            void it_returns_updated_user() {
                assertThatThrownBy(() -> userService.updateInfo(INVALID_ID, userUpdateInfoData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

    }


    @Nested
    @DisplayName("deleteUser 메소드는")
    class Describe_deleteUser {

        @DataJpaTest
        @Nested
        @DisplayName("존재하는 아이디가 주어지면")
        class Context_with_existed_id {
            private Long id;

            @BeforeEach
            void setUp() {
                User savedUser = userRepository.save(user);
                id = savedUser.getId();
            }

            @Test
            @DisplayName("user가 삭제된다.")
            void it_will_be_deleted_user() {
                userService.deleteUser(id);

                assertThatThrownBy(() -> userRepository.findById(id).get())
                        .isInstanceOf(NoSuchElementException.class);
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("존재하지 않는 아이디가 주어지면")
        class Context_with_not_existed_id {

            private final Long INVALID_ID = 100L;

            @Test
            @DisplayName("UserNotFoundException 예외를 던진다.")
            void it_returns_updated_user() {
                assertThatThrownBy(() -> userService.deleteUser(INVALID_ID))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

    }

}
