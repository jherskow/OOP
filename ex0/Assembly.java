// File:       Assembly.java
// Author:     Josuha Herskowitz , jherskow , 321658379
// Exercise:   OOP ex0 2017
//

/**
 * This class represents the Assembly of the Israeli Legislative Body.
 * @author Josh Herskowitz
 */
class Assembly {

	private static int apiErrorCode = -1;
	private static int maxKnessetMembers = 120;

	private int lawCapacity;
	private int maxLawsPerKnessetMember;
	private Law[] laws;
	private int[] surveyScoreList;
	private KnessetMember[] knessetMembers;
	private int[] lawsSupportedByknessetMember;
	private int currentLawCount;
	private int currentKnessetMemberCount;


   /*----=  Constructors  =-----*/

	/**
	 * Creates a new assembly with the given parameters.
	 * @param maxLawCapacity The maximal number of laws this assembly can hold.
	 * @param maxSupportedLawsPerKnessetMember The maximal number of laws this assembly allows a single KnessetMember to support at the
	 * same time.
	 */
	public Assembly(int maxLawCapacity, int maxSupportedLawsPerKnessetMember){
		lawCapacity = maxLawCapacity;
		maxLawsPerKnessetMember = maxSupportedLawsPerKnessetMember;
		currentLawCount = 0;
		surveyScoreList = new int[maxLawCapacity];
		laws = new Law[maxLawCapacity];
		knessetMembers = new KnessetMember[maxKnessetMembers];
		currentKnessetMemberCount = 0;
		lawsSupportedByknessetMember = new int[maxKnessetMembers];
		for (int i = 0; i < maxKnessetMembers ; i++) {
			lawsSupportedByknessetMember[i] = 0;
		}
	}

   /*----=  Instance Methods  =-----*/

	/**
	 * Adds the given law to this assembly, if there is place available, and it isn't already in the assembly.
	 * @param law The law to add to this library.
	 * @param surveyResult The survey result of the law.
	 * @return a non-negative id number for the law if there was a spot and the law was successfully
	 * added, or if the law was already in the assembly; a negative number otherwise.
	 */
	public int addLawToAssembly(Law law, int surveyResult){
		int currentLawId = getLawId(law);
		if (currentLawId < 0){
			if (currentLawCount < lawCapacity){
				laws[currentLawCount] = law;
				surveyScoreList[currentLawCount] = surveyResult;
				currentLawCount++;
				return (currentLawCount - 1);
			}else{
				return apiErrorCode;
			}
		}else{
			return currentLawId;
		}
	}

	/**
	 * updates the survey result of lawId with a new value
	 * @param law law to be updated
	 * @param newSurveyValue new survey value.
	 */
	public void updateSurveyResultOfLaw(Law law, int newSurveyValue){
		int currentLawId = getLawId(law);
		if (isLawIDValid(currentLawId)){
			surveyScoreList[currentLawId] = newSurveyValue;
		}
	}

	/**
	 * Returns true if the given number is an id of some law in the assembly, false otherwise.
	 * @param lawId The id to check.
	 * @return true if the given number is an id of some law in the assembly, false otherwise.
	 */
	public boolean isLawIDValid(int lawId){
		return (0 <=lawId && lawId < currentLawCount);
	}

	/**
	 * Returns the non-negative id number of the given law if he is discussed by this assembly, -1 otherwise.
	 * @param law The law for which to find the id number.
	 * @return a non-negative id number of the given law if he is discussed by this assembly, -1 otherwise.
	 */
	public int getLawId(Law law){
		for (int i = 0; i < currentLawCount; i++) {
			if (laws[i] == law) {
				return i;
			}
		}
		return apiErrorCode;
	}

	/**
	 * Registers the given KnessetMember to this assembly, if there is a spot available.
	 * @param KnessetMember The KnessetMember to register to this assembly.
	 * @return a non-negative id number for the KnessetMember if there was a spot and the patron was successfully
	 * registered, a negative number otherwise.
	 */
	public int registerKnessetMember(KnessetMember KnessetMember){
		if (currentKnessetMemberCount < maxKnessetMembers){
			knessetMembers[currentKnessetMemberCount] = KnessetMember;
			currentKnessetMemberCount++;
			return (currentKnessetMemberCount - 1);
		}else{
			return apiErrorCode;
		}
	}

	/**
	 * Returns true if the given number is an id of a KnessetMember in the assembly, false otherwise.
	 * @param KnessetMemberId The id to check.
	 * @return  true if the given number is an id of a KnessetMember in the assembly, false otherwise.
	 */
	public boolean isKnessetMemberIdValid(int KnessetMemberId){
		return (0 <= KnessetMemberId && KnessetMemberId < currentKnessetMemberCount);
	}

	/**
	 * Returns the non-negative id number of the given KnessetMember if he is registered to this assembly, -1 otherwise.
	 * @param KnessetMember The KnessetMember for which to find the id number.
	 * @return a non-negative id number of the given KnessetMember if he is registered to this assembly, -1 otherwise.
	 */
	public int getKnessetMemberId(KnessetMember KnessetMember){
		for (int i = 0; i < currentKnessetMemberCount; i++) {  //wrong return value
			if (knessetMembers[i] == KnessetMember) {
				return i;
			}
		}
		return apiErrorCode;
	}

	/**
	 * adds KnessetMember to law, if the KnessetMember will support the law according to the survey results.
	 * @param lawId The id number of the law to borrow.
	 * @param KnessetMemberId The id number of the KnessetMember that will support the law.
	 * @param surveyResult The survey result of the law to support.
	 * @return true if the KnessetMember was added successfully, false otherwise.
	 */
	public boolean supportLaw(int lawId, int KnessetMemberId, int surveyResult){
		int currentLawsAlreadySupported = lawsSupportedByknessetMember[KnessetMemberId];
		if (!(isLawIDValid(lawId)) || !(isKnessetMemberIdValid(KnessetMemberId))
							|| (currentLawsAlreadySupported >= maxLawsPerKnessetMember) ){
			return false;
		}
		if(knessetMembers[KnessetMemberId].willJoinLaw(laws[lawId], surveyResult)){
			laws[lawId].addJoinedKnessetMember();
			lawsSupportedByknessetMember[KnessetMemberId]++;
			return true;
		}
		return false;
	} //null pinter exception??

	/**
	 * Suggest to the KnessetMember with the given id a law which suits him the best (maximum score of the laws in the assembly).
	 * @param KnessetMemberId The id number of the KnessetMember to suggest the law to.
	 * @return The best law to match the KnessetMember preferences. Null if there aren't any (case all laws get a zero score).
	 * available.
	 */
	public Law suggestLawToKnessetMember(int KnessetMemberId){
		double bestLawScore = 0;
		double newLawScore;
		int bestLawId = 0;
		Law currentLaw;
		KnessetMember currentKnessetMember = knessetMembers[KnessetMemberId];
		for (int i = 0; i < currentLawCount; i++) {
			currentLaw = laws[i];
			newLawScore = currentKnessetMember.getLawScore(currentLaw, surveyScoreList[i]);
			if (newLawScore > bestLawScore) {
				bestLawId = i;
				bestLawScore = newLawScore;
			}
		}
		if (bestLawId > 0){
			return laws[bestLawId];
		}
		return null;
	}
}

