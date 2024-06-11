package com.ftn.sbnz.service.services.implementations;

import com.ftn.sbnz.model.events.ShouldBanUserEvent;
import com.ftn.sbnz.model.events.UnbanUserEvent;
import com.ftn.sbnz.model.models.Car;
import com.ftn.sbnz.model.models.CarLike;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.service.repositories.BaseJPARepository;
import com.ftn.sbnz.service.repositories.CarLikeRepository;
import com.ftn.sbnz.service.repositories.UserRepository;
import com.ftn.sbnz.service.services.ICarService;
import com.ftn.sbnz.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static com.ftn.sbnz.service.services.KieService.cepKsession;


@Service
public class UserService extends BaseService<User> implements IUserService {
    private final UserRepository userRepository;
    private final CarLikeRepository carLikeRepository;
    private final ICarService carService;

    @Autowired
    public UserService(UserRepository userRepository, CarLikeRepository carLikeRepository, ICarService carService) {
        this.userRepository = userRepository;
        this.carLikeRepository = carLikeRepository;
        this.carService = carService;
    }

    @Override
    protected BaseJPARepository<User> getRepository() {
        return this.userRepository;
    }

    @Override
    public boolean userExists(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean createCarLike(String email, Long carId) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user == null || user.isLikeBanned()) {
            return false;
        }

        Car car = carService.findById(carId);

        CarLike carLike = new CarLike(user, car);
        carLike = this.carLikeRepository.save(carLike);

        Queue<ShouldBanUserEvent> eventQueue = new LinkedList<>();
        cepKsession.setGlobal("eventQueue", eventQueue);
        cepKsession.insert(carLike);
        cepKsession.fireAllRules();

        // Ban user from liking
        for (ShouldBanUserEvent event : eventQueue) {
            Optional<User> optionalUserToBan = userRepository.findById(event.getUserId());
            if (optionalUserToBan.isPresent()) {
                User userToBan = optionalUserToBan.get();
                if (!userToBan.isLikeBanned()) {
                    userToBan.setLikeBanned(true);
                    this.userRepository.save(userToBan);

                    System.out.println("Banned user: " + userToBan.getEmail());
                }
            }
        }

        eventQueue.clear();
        return true;
    }

    @Override
    public void unbanUser(String email) {
        User user = this.userRepository.findByEmail(email).orElse(null);

        assert user != null;
        UnbanUserEvent unbanUserEvent = new UnbanUserEvent(user.getId());

        Queue<UnbanUserEvent> eventUnbanQueue = new LinkedList<>();
        cepKsession.setGlobal("eventUnbanQueue", eventUnbanQueue);
        cepKsession.insert(unbanUserEvent);
        cepKsession.fireAllRules();

        for (UnbanUserEvent event : eventUnbanQueue) {
            Optional<User> optionalUserToUnban = userRepository.findById(event.getUserId());
            if (optionalUserToUnban.isPresent()) {
                User userToUnban = optionalUserToUnban.get();
                if (userToUnban.isLikeBanned()) {
                    userToUnban.setLikeBanned(false);
                    this.userRepository.save(userToUnban);

                    System.out.println("Unbanned user: " + userToUnban.getEmail());
                }
            }
        }
    }
}
