package bd.ohedulalam.polls.controller;

import bd.ohedulalam.polls.exception.ResourceNotFountException;
import bd.ohedulalam.polls.model.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser){
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping(value = "/user/checkUserNameAvailability")
    public UserIdentityAvaivality checkUserNameAvailability(@RequestParam(value = "username") String username){
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvaivality(isAvailable);
    }

    @GetMapping(value = "/user/checkEmailAvailability")
    public UserIdentityAvaivality checkEmailAvailability(@RequestParam(value = "email") String email){
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvaivality(isAvailable);
    }

    @GetMapping(value = "/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFountException("User", "username", username));
        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(),user.getName(),
                user.getCreatedAt(), pollCount, voteCount);
        return userProfile;
    }

    @GetMapping(value = "/users/{username}/polls")
    public PagedResponse<PollResponse> getPollCreatedBy(@PathVariable(value = "username") String username,
                                                        @CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(value ="page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getPollsCreatedBy(username,currentUser,page,size);
    }

    @GetMapping(value = "users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getPollsVotedBy(username, currentUser, page,size);
    }

}
