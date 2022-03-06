package grabit.grabit_backend.Service;

import grabit.grabit_backend.DTO.CreateChallengeDTO;
import grabit.grabit_backend.DTO.FindChallengeDTO;
import grabit.grabit_backend.DTO.ModifyChallengeDTO;
import grabit.grabit_backend.DTO.ResponseChallengeDTO;
import grabit.grabit_backend.Domain.Challenge;
import grabit.grabit_backend.Domain.User;
import grabit.grabit_backend.Domain.UserChallenge;
import grabit.grabit_backend.Repository.ChallengeRepository;
import grabit.grabit_backend.Repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;
	private final UserChallengeRepository userChallengeRepository;

	@Autowired
	public ChallengeService(ChallengeRepository challengeRepository, UserChallengeRepository userChallengeRepository){
		this.challengeRepository = challengeRepository;
		this.userChallengeRepository = userChallengeRepository;
	}

	/**
	 * 챌린지 생성
	 * @param createChallengeDTO
	 * @return id
	 */
	@Transactional
	public ResponseChallengeDTO createChallenge(CreateChallengeDTO createChallengeDTO, User leader){
		Challenge challenge = new Challenge(leader,createChallengeDTO.getName(), createChallengeDTO.getChallengeDesc());
		Challenge createChallenge = challengeRepository.save(challenge);
		// 유저 아이디 정보 확인.
		UserChallenge userChallenge = new UserChallenge();
		userChallenge.setChallenge(createChallenge);
		userChallenge.setUser(leader);
		userChallengeRepository.save(userChallenge);
		return ResponseChallengeDTO.convertDTO(createChallenge);
	}

	/**
	 * 챌린지 검색 (id)
	 * @param id
	 * @return Challenge
	 */
	public ResponseChallengeDTO findChallengeById(Long id){
		Optional<Challenge> findChallenge = challengeRepository.findById(id);
		if(findChallenge.isEmpty()){
			throw new IllegalStateException("존재하지 않는 챌린지입니다..");
		}
		Challenge ChallengeDomain = findChallenge.get();
		return ResponseChallengeDTO.convertDTO(ChallengeDomain);
	}

	/**
	 * 챌린지 검색 (name)
	 * @param name
	 * @return List of Challenge
	 */
	public ArrayList<ResponseChallengeDTO> findChallengeByName(String name){
		List<Challenge> findChallenges = challengeRepository.findByName(name);
		ArrayList<ResponseChallengeDTO> returnChallenges = new ArrayList<>();
		for(Challenge challenge: findChallenges){
			returnChallenges.add(ResponseChallengeDTO.convertDTO(challenge));
		}
		return returnChallenges;
	}

	/**
	 * 챌린지 삭제
	 * @param id
	 */
	public void deleteChallengeById(Long id){
		// 유저 아이디 정보 확인.
		Optional<Challenge> findChallenge = challengeRepository.findById(id);
		if(findChallenge.isEmpty()){
			throw new IllegalStateException("존재하지 않는 챌린지입니다..");
		}
		challengeRepository.deleteById(id);
	}

	/**
	 * 챌린지 수정
	 * @param id
	 * @param afterChallenge
	 * @return Challenge
	 */
	public ResponseChallengeDTO updateChallenge(ModifyChallengeDTO modifyChallengeDTO){
		// 유저 아이디 정보 확인.
		Optional<Challenge> findChallenge = challengeRepository.findById(modifyChallengeDTO.getId());
		if(findChallenge.isEmpty()){
			throw new IllegalStateException("존재하지 않는 챌린지입니다..");
		}
		findChallenge.get().setName(modifyChallengeDTO.getName());
//		findChallenge.get().setLeaderId(modifyChallengeDTO.getLeaderId());
		findChallenge.get().setChallengeDesc(modifyChallengeDTO.getChallengeDesc());
		Challenge modifyChallenge = challengeRepository.save(findChallenge.get());
		return ResponseChallengeDTO.convertDTO(modifyChallenge);
	}

	/**
	 * 모든 챌린지 검색
	 * @return ArrayList of ResponseChallengeDTO
	 */
	public ArrayList<ResponseChallengeDTO> findAllChallenge(){
		List<Challenge> findChallenges = challengeRepository.findAll();
		ArrayList<ResponseChallengeDTO> returnChallenges = new ArrayList<>();
		for(Challenge challenge: findChallenges){
			returnChallenges.add(ResponseChallengeDTO.convertDTO(challenge));
		}
		return returnChallenges;
	}

}
