// File:        Law.java
// Author:      Josuha Herskowitz , jherskow , 321658379
// Exercise:    OOP ex0 2017
//

/**
 * This class represents a law.
 * @author Josh Herskowitz
 */
class Law {

	public static int defaultLawSupport = 1;

	private String title;
	private KnessetMember initiator;
	private String party;
	private int yearOfPublication;
	public int socialValue ;
	public int economyValue;
	public int politicalValue;
	private int supportingKnessetMemebers;
	private String partyOfInitiator;

   /*----=  Constructors  =-----*/

	/**
	 * Creates a new law with the given characteristic.
	 * @param lawTitle The title of the law.
	 * @param lawInitiator The initiator of the law.
	 * @param lawParty The party of the lawInitiator
	 * @param lawYearOfPublication The year the book was published.
	 * @param lawSocialValue The comic value of the book.
	 * @param lawEconomyValue The dramatic value of the book.
	 * @param lawPoliticalValue The educational value of the book.
	 */
	Law(String lawTitle, KnessetMember lawInitiator, String lawParty, int lawYearOfPublication, int lawSocialValue, int lawEconomyValue,
		int lawPoliticalValue){
		title = lawTitle;
		initiator = lawInitiator;
		party = lawParty;
		yearOfPublication = lawYearOfPublication;
		socialValue = lawSocialValue;
		economyValue = lawEconomyValue;
		politicalValue = lawPoliticalValue;
		//KnessetMember[] supportingKnessetMemebers = new KnessetMember[120];
		supportingKnessetMemebers = defaultLawSupport;
		partyOfInitiator = lawParty;
	}

   /*----=  Instance Methods  =-----*/

	/**
	 * Returns a string representation of the law, which is a sequence
	 * of the title, initiator name and year of publication, separated by
	 * commas, enclosed in square brackets. For example, if the law is
	 * titled "Fix 128 to Bituah Leumi order", was initiated by Eli Alaluf and published in 2016,
	 * this method will return the string:
	 * "[Fix 128 to Bituah Leumi order,Eli Alaluf,Kulanu,2016]"
	 * @return the String representation of this law.
	 */
	String stringRepresentation(){
		return "["+title+","+initiator.stringRepresentation()+","+ partyOfInitiator +","+yearOfPublication+"]";
	}

	/**
	 * Adds another MK to support this law
	 */
	void addJoinedKnessetMember(){
		supportingKnessetMemebers++;
	}

	/**
	 * remove a single MK support. If only 1 MK supports the law, do nothing.
	 */
	void subJoinedKnessetMember(){
		if (supportingKnessetMemebers > defaultLawSupport) {
			supportingKnessetMemebers--;
		}
	}

	/**
	 * Returns the number of MKs that are currently supporting this Law (including initiator)
	 * @return number of Knesset Members that are currently support this law
	 */
	int getCurrentNumberOfKnessetMembers(){
		return supportingKnessetMemebers;
	}
}