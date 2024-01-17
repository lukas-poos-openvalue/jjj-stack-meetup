package de.lpo.todo.domain;

import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.repository.BoardRepo;
import de.lpo.todo.domain.utils.*;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardServiceTest {
    private final BoardRepo boardRepo = mock();
    private final BoardService boardService = new BoardService(boardRepo);

    private static Board oneBoard(Id<Integer> boardId) {
        return new Board(boardId, "title", Refs.unloaded(), Ref.unloaded(StringId.ofValue("a")), Refs.unloaded());
    }

    @Test
    void should_add() {
        // GIVEN
        // - Board data
        final var title = RandomStringUtils.random(10);
        final var ownerId = StringId.ofValue(RandomStringUtils.random(2));
        final var memberIdHashes = List.of(
                StringId.ofValue(RandomStringUtils.random(2)),
                StringId.ofValue(RandomStringUtils.random(2)),
                StringId.ofValue(RandomStringUtils.random(2))
        );

        // - Mock repo
        final var boardId = IntId.ofValue(RandomUtils.nextInt());
        when(boardRepo.add(any(), any(), any())).thenReturn(boardId);
        final var board = oneBoard(boardId);
        when(boardRepo.findOneByIdFetchAll(boardId)).thenReturn(Optional.of(board));

        // WHEN
        final var result = boardService.addFetchAll(title, ownerId, memberIdHashes);

        // THEN
        assertThat(result).isEqualTo(board);
        verify(boardRepo).add(title, ownerId, memberIdHashes);
        verify(boardRepo).findOneByIdFetchAll(boardId);
    }

    @Test
    void should_update() {
        // GIVEN
        final var boardId = IntId.ofValue(RandomUtils.nextInt());
        final var board = oneBoard(boardId);
        final var title = RandomStringUtils.random(10);
        final var memberIdHashes = List.of(
                StringId.ofValue(RandomStringUtils.random(2)),
                StringId.ofValue(RandomStringUtils.random(2)),
                StringId.ofValue(RandomStringUtils.random(2))
        );

        // - Mock repo
        when(boardRepo.findOneByIdFetchAll(boardId)).thenReturn(Optional.of(board));

        // WHEN
        final var result = boardService.updateFetchAll(boardId, title, memberIdHashes);

        // THEN
        assertThat(result).isEqualTo(board);
        verify(boardRepo).update(board.id(), title, memberIdHashes);
        verify(boardRepo).findOneByIdFetchAll(board.id());
    }

    @Test
    void should_delete() {
        // GIVEN
        // - Existing IDs
        final var boardId = IntId.ofValue(RandomUtils.nextInt());

        // WHEN
        boardService.deleteById(boardId);

        // THEN
        verify(boardRepo).deleteById(boardId);
    }
}