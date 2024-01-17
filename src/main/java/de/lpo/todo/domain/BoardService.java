package de.lpo.todo.domain;

import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.repository.BoardRepo;
import de.lpo.todo.domain.utils.Id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Service for managing {@link Board boards}.
 * Provides the basic CRUD functionality.
 */
public class BoardService {
    private final BoardRepo boardRepo;

    public BoardService( BoardRepo boardRepo) {
        this.boardRepo = boardRepo;
    }

    /**
     * Adds a new {@link Board} to the database.
     * @param title the title
     * @param ownerId the id of the {@link User owner}
     * @param memberIds the ids of the {@link User members}
     * @return the new {@link Board board-instance}, with all references loaded.
     */
    public Board addFetchAll(String title, Id<String> ownerId, List<Id<String>> memberIds) {
        final var boardId = boardRepo.add(title, ownerId, memberIds);
        return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
    }

    /**
     * Selects all the boards that the given {@link User} belongs to.
     * @param user the {@link User}
     * @return a list of {@link Board boards}
     */
    public List<Board> findAllForUserFetchAll(User user) {
        return boardRepo.findAllForUserFetchAll(user);
    }

    /**
     * Selects one {@link Board} from the database by the given id.
     * @param boardId the hashed id
     * @return the value of the {@link Board} found or an empty result
     */
    public Board getOneByIdFetchAll(@NotNull Id<Integer> boardId) {
        return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
    }

    /**
     * Performs updates to the given {@link Board}.
     * @param boardId the hashed id
     * @param title the title (remains untouched if null)
     * @param memberIds (remain untouched if null)
     * @return the {@link Board} with the updated values, with all references loaded
     */
    public Board updateFetchAll(@NotNull Id<Integer> boardId, @Nullable String title, @Nullable List<Id<String>> memberIds) {
        boardRepo.update(boardId, title, memberIds);
        return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
    }

    /**
     * Deletes a {@link Board}
     * @param boardId the hashed id
     */
    public void deleteById(@NotNull Id<Integer> boardId) {
        boardRepo.deleteById(boardId);
    }

    public static class BoardNotFoundException extends RuntimeException {
        public BoardNotFoundException() {
            super("No board was found with the given ID!");
        }
    }
}
