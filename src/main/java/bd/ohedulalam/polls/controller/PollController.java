package bd.ohedulalam.polls.controller;

import bd.ohedulalam.polls.model.Poll;
import bd.ohedulalam.polls.payload.*;
import bd.ohedulalam.polls.repository.PollRepository;
import bd.ohedulalam.polls.repository.UserRepository;
import bd.ohedulalam.polls.repository.VoteRepository;
import bd.ohedulalam.polls.security.CurrentUser;
import bd.ohedulalam.polls.security.UserPrincipal;
import bd.ohedulalam.polls.service.PollService;
import bd.ohedulalam.polls.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getAllPolls(currentUser, page,size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest){
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{pollId}").buildAndExpand(poll.getId()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true,"Poll created successfully."));
    }

    @GetMapping(value = "/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId){
        return pollService.getPollById(pollId,currentUser);
    }

    @PostMapping(value = "/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse createVote(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long pollId, @Valid @RequestBody VoteRequest voteRequest){
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    }


}
