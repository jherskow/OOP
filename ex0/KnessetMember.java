// File:        KnessetMember.java
// Author:      Josuha Herskowitz , jherskow , 321658379
// Exercise:    OOP ex0 2017
//

/**
 * This class represents a Knesset Member.
 * @author Josh Herskowitz
 */
class KnessetMember {

	public static double knessetMembersEnthusiasmThreshold = 5;

	private String firstName;
	private String lastName;
	private double mkSocialTendency;
	private double mkEconomyTendency;
	private double mkPoliticalTendency;
	private int surveyThreshold;

   /*----=  Constructors  =-----*/

	/**
	 * Creates a new KnessetMember with the given characteristics.
	 *
	 * @param knessetMemberFirstName       The first name of the KnessetMember.
	 * @param knessetMemberLastName        The last name of the KnessetMember.
	 * @param socialTendency               The weight the KnessetMember assigns to the social aspects of laws.
	 * @param economyTendency              The weight the KnessetMember assigns to the economy aspects of laws.
	 * @param politicalTendency            The weight the KnessetMember assigns to the political aspects of laws.
	 * @param knessetMemberSurveyThreshold The minimal public support a law must have for this KnessetMember to join it.
	 */
	public KnessetMember(String knessetMemberFirstName, String knessetMemberLastName, double socialTendency, double economyTendency,
				  double politicalTendency, int knessetMemberSurveyThreshold) {
		firstName = knessetMemberFirstName;
		lastName = knessetMemberLastName;
		mkSocialTendency = socialTendency;
		mkEconomyTendency = economyTendency;
		mkPoliticalTendency = politicalTendency;
		surveyThreshold = knessetMemberSurveyThreshold;

	}

   /*----=  Instance Methods  =-----*/

	/**
	 * Returns a string representation of the KnessetMember, which is a sequence of its first name, last name and title
	 * separated by a single white space. For example, if the KnessetMember's first name is "Yehudah" and his last name
	 * is "Glick", this method will return the String "Knesset Member Yehudah Glick".
	 *
	 * @return the String representation of this KnessetMember.
	 */
	public String stringRepresentation() {
		return ("Knesset Member " + firstName + " " + lastName);
	}

	/**
	 * Returns the interest value this KnessetMember assigns to the given Law.
	 *
	 * @param law          The law to assess.
	 * @param surveyResult The result of a survey made for this law
	 * @return the interest value this KnessetMember assigns to the given law. 0 if survey result does not pass threshold.
	 */
	public double getLawScore(Law law, int surveyResult) {
		if (surveyResult < surveyThreshold) {
			return 0;
		}
		double socialScore = law.socialValue * mkSocialTendency;
		double economyScore = law.economyValue * mkEconomyTendency;
		double politicalScore = law.politicalValue * mkPoliticalTendency;
		return (socialScore + economyScore + politicalScore);
	}

	/**
	 * Returns true of this KnessetMember will join the given law (law score is bigger than knessetMembersEnthusiasmThreshold), false otherwise.
	 *
	 * @param law The law to asses.
	 * @return true of this KnessetMember will join the given law, false otherwise.
	 */
	public boolean willJoinLaw(Law law, int surveyResult) {
		double lawScore = getLawScore(law, surveyResult);
		return (lawScore > knessetMembersEnthusiasmThreshold);
	}
}
