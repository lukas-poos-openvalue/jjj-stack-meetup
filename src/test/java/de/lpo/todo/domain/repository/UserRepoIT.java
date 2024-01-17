package de.lpo.todo.domain.repository;


import de.lpo.todo.domain.utils.StringId;
import de.lpo.utils.IntegrationTestDomain;
import de.lpo.utils.DomainTestUtils;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepoIT extends IntegrationTestDomain {

    @Test
    void should_confirm_that_user_is_owner_of_board() {
        // GIVEN
        // - Test-scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var owner = board.owner();

        // WHEN
        final var result = appCtx.userRepo.isUserOwnerOrMemberOfBoard(owner.id(), board.id());

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void should_confirm_that_user_is_member_of_board() {
        // GIVEN
        // - Test-scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var member = board.members().unwrap().getFirst();

        // WHEN
        final var result = appCtx.userRepo.isUserOwnerOrMemberOfBoard(member.id(), board.id());

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void should_reject_that_user_is_owner_or_member_of_board() {
        // GIVEN
        // - Test-scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var userId = StringId.ofValue(RandomStringUtils.random(10));

        // WHEN
        final var result = appCtx.userRepo.isUserOwnerOrMemberOfBoard(userId, board.id());

        // THEN
        assertThat(result).isFalse();
    }

}