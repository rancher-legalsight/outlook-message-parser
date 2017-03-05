package org.simplejavamail.outlookmessageparser;

import org.junit.Test;
import org.simplejavamail.outlookmessageparser.attachment.Attachment;
import org.simplejavamail.outlookmessageparser.attachment.FileAttachment;
import org.simplejavamail.outlookmessageparser.attachment.MsgAttachment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {

	@Test
	public void testRtfSent()
			throws IOException {
		Message msg = parseMsgFile("simple sent.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("John Doe");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail("jdoes@someserver.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("(outlookEMLandMSGconverter Trial Version Import) BitDaddys Software");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("sales@bitdaddys.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("sales@bitdaddys.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasNoAttachments();
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNull();
		assertThat(msg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("Dear BitDaddys Corp.,\n"
				+ "\n"
				+ "We have added your software to our approved list.\n"
				+ "\n"
				+ "Thank you for your efforts,\n"
				+ "Sincerely,\n"
				+ "John Doe\n"
				+ "Some Server Company\n");
	}

	@Test
	public void testUnsentRtfDraft()
			throws IOException {
		Message msg = parseMsgFile("unsent draft.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("MSG Test File");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("time2talk@online-convert.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("time2talk@online-convert.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasNoAttachments();
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNull();
		assertThat(msg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("MSG test file\n"
				+ "Purpose: Provide example of this file type\n"
				+ "Document file type: MSG\n"
				+ "Version: 1.0\n"
				+ "Remark:\n"
				+ "\n"
				+ "Example content:\n"
				+ "The names \"John Doe\" for males, \"Jane Doe\" or \"Jane Roe\" for females,\n"
				+ "or \"Jonnie Doe\" and \"Janie Doe\" for children, or just \"Doe\"\n"
				+ "non-gender-specifically are used as placeholder names for a party whose\n"
				+ "true identity is unknown or must be withheld in a legal action, case, or\n"
				+ "discussion. The names are also used to refer to acorpse or hospital\n"
				+ "patient whose identity is unknown. This practice is widely used in the\n"
				+ "United States and Canada, but is rarely used in other English-speaking\n"
				+ "countries including the United Kingdom itself, from where the use of\n"
				+ "\"John Doe\" in a legal context originates. The names Joe Bloggs or John\n"
				+ "Smith are used in the UK instead, as well as in Australia and New\n"
				+ "Zealand. \n"
				+ "\n"
				+ "John Doe is sometimes used to refer to a typical male in other contexts\n"
				+ "as well, in a similar manner to John Q. Public, known in Great Britain\n"
				+ "as Joe Public, John Smith or Joe Bloggs. For example, the first name\n"
				+ "listed on a form is often John Doe, along with a fictional address or\n"
				+ "other fictional information to provide an example of how to fill in the\n"
				+ "form. The name is also used frequently in popular culture, for example\n"
				+ "in the Frank Capra film Meet John Doe. John Doe was also the name of a\n"
				+ "2002 American television series. \n"
				+ "\n"
				+ "Similarly, a child or baby whose identity is unknown may be referred to\n"
				+ "as Baby Doe. A notorious murder case in Kansas City, Missouri, referred\n"
				+ "to the baby victim as Precious Doe. Other unidentified female murder\n"
				+ "victims are Cali Doe and Princess Doe. Additional persons may be called\n"
				+ "James Doe, Judy Doe, etc. However, to avoid possible confusion, if two\n"
				+ "anonymous or unknown parties are cited in a specific case or action, the\n"
				+ "surnames Doe and Roe may be used simultaneously; for example, \"John Doe\n"
				+ "v. Jane Roe\". If several anonymous parties are referenced, they may\n"
				+ "simply be labelled John Doe #1, John Doe #2, etc. (the U.S. Operation\n"
				+ "Delego cited 21 (numbered) \"John Doe\"s) or labelled with other variants\n"
				+ "of Doe / Roe / Poe / etc. Other early alternatives such as John Stiles\n"
				+ "and Richard Miles are now rarely used, and Mary Major has been used in\n"
				+ "some American federal cases. \n"
				+ "\n"
				+ "File created by http://www.online-convert.com\n"
				+ "<http://www.online-convert.com> \n"
				+ "More example files: http://www.online-convert.com/file-type\n"
				+ "<http://www.online-convert.com/file-type> \n"
				+ "Text of Example content: Wikipedia\n"
				+ "<http://en.wikipedia.org/wiki/John_Doe> \n"
				+ "License: Attribution-ShareAlike 3.0 Unported\n"
				+ "<http://creativecommons.org/licenses/by-sa/3.0/> \n"
				+ "\n"
				+ "Feel free to use and share the file according to the license above.\n");
	}

	@Test
	public void testHtmlMessageChain()
			throws IOException {
		Message msg = parseMsgFile("plain chain.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("Robert Duncan");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail(null);
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("RE: [Redmine - Bug #10180] redmine not truncating emails properly");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasRecipients(
				createRecipient("'applsoft-redmine@anca.com'", "applsoft-redmine@anca.com"),
				createRecipient("'rob@thegopedal.com'", "rob@thegopedal.com"));
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasNoAttachments();
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNotEmpty();
		assertThat(msg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("Another test email to obtain raw data for Jean-Philippe.\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Regards,\n"
				+ "\n"
				+ "Robert Duncan\n"
				+ "\n"
				+ "ph. 8277\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "From: applsoft-redmine@anca.com [mailto:applsoft-redmine@anca.com] \n"
				+ "Sent: Friday, 5 October 2012 12:45 PM\n"
				+ "To: robertd@anca.com; Eng Tan\n"
				+ "Subject: [Redmine - Bug #10180] redmine not truncating emails properly\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Issue #10180 has been updated by Robert Duncan. \n"
				+ "\n"
				+ "Reported to redmine http://www.redmine.org/issues/12025\n"
				+ "\n"
				+ "________________________________\n"
				+ "\n"
				+ "\n"
				+ "Bug #10180: redmine not truncating emails properly\n"
				+ "<http://anca-redmine.anca.com.au/applsoft/issues/10180#change-5787> \n"
				+ "\n"
				+ "\n"
				+ "*\tAuthor: Robert Duncan\n"
				+ "*\tStatus: New\n"
				+ "*\tPriority: Normal\n"
				+ "*\tAssignee: Robert Duncan\n"
				+ "*\tCategory: \n"
				+ "*\tTarget version: \n"
				+ "\n"
				+ "Redmine should truncate emails after lines such as \"Regards,\". \n"
				+ "It did not do this, and included the entire email as a one-line comment\n"
				+ "for the issue.\n"
				+ "\n"
				+ "________________________________\n"
				+ "\n"
				+ "You have received this notification because you have either subscribed\n"
				+ "to it, or are involved in it.\n"
				+ "To change your notification preferences, please click here:\n"
				+ "http://anca-redmine.anca.com.au/applsoft/my/account\n\n");
	}

	@Test
	public void testNestedRtfMsg()
			throws IOException {
		Message msg = parseMsgFile("nested simple mail.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("REISINGER Emanuel");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail("Emanuel.Reisinger@cargonet.software");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("outlookmsg2html Testmail");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("REISINGER Emanuel");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("Emanuel.Reisinger@cargonet.software");
		List<Attachment> attachments = msg.getAttachments();
		assertThat(attachments).hasSize(1);
		Attachment attachment = attachments.get(0);
		assertThat(attachment).isOfAnyClassIn(MsgAttachment.class);
		Message nestedMsg = ((MsgAttachment) attachment).getMessage();
		/* nested message */
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasFromName("REISINGER Emanuel");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasFromEmail("Emanuel.Reisinger@cargonet.software");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasSubject("outlookmsg2html Testmail");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasToName("REISINGER Emanuel");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasToEmail("Emanuel.Reisinger@cargonet.software");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(nestedMsg).hasNoAttachments();
		assertThat(nestedMsg.getBodyText()).isNotEmpty();
		assertThat(nestedMsg.getBodyHTML()).isNull();
		assertThat(nestedMsg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(nestedMsg.getBodyText())).isEqualTo("Hello all,\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "This is a testmail.\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "BR\n"
				+ "\n"
				+ "Hui Pui \n"
				+ "\n");
		/* /nested message */
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNotEmpty();
		assertThat(msg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("Mail in mail.\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "BR\n"
				+ "\n");
	}

	@Test
	public void testFileAttachments()
			throws IOException {
		Message msg = parseMsgFile("attachments.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("Microsoft Outlook");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail("MicrosoftExchange329e71ec88ae4615bbc36ab6ce41109e@coab.us");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("Delivery delayed:RE: Bosco Fraud Cases [ 2 of 8]");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("darlington@coab.us");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("darlington@coab.us");
		List<Attachment> attachments = msg.getAttachments();
		assertThat(attachments).hasSize(2);
		Attachment attachment1 = attachments.get(0);
		Attachment attachment2 = attachments.get(1);
		assertAttachmentMetadata(attachment1, "message/delivery-status", null, null);
		assertAttachmentMetadata(attachment2, "text/rfc822-headers", null, null);
		assertThat(attachment1).isOfAnyClassIn(FileAttachment.class);
		assertThat(attachment2).isOfAnyClassIn(FileAttachment.class);
		String attachmentContent1 = normalizeText(new String(((FileAttachment) attachment1).getData(), UTF_8));
		String attachmentContent2 = normalizeText(new String(((FileAttachment) attachment2).getData(), UTF_8));
		assertThat(attachmentContent1).isEqualTo("Reporting-MTA: dns;ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Received-From-MTA: dns;ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Arrival-Date: Mon, 11 Apr 2016 13:08:47 +0000\n"
				+ "\n"
				+ "Final-Recipient: rfc822;Darren.vuzzo@myfloridalicence.com\n"
				+ "Action: delayed\n"
				+ "Status: 4.4.7\n"
				+ "Diagnostic-Code: smtp;400 4.4.7 Message delayed\n"
				+ "Will-Retry-Until: Wed, 13 Apr 2016 09:08:47 -0400\n"
				+ "X-Display-Name: Darren.vuzzo@myfloridalicence.com\n");
		assertThat(attachmentContent2).isEqualTo("Received: from ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) by\n"
				+ " ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) with Microsoft SMTP Server\n"
				+ " (TLS) id 15.0.1076.9; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563])\n"
				+ " by ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563%17]) with\n"
				+ " mapi id 15.00.1076.000; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "From: \"Arlington, Daniel\" <darlington@coab.us>\n"
				+ "To: ERICKSON ENERGY <ericksonenergy@gmail.com>, \"Reagan, Raina\"\n"
				+ "\t<Raina.Reagan@myfloridalicense.com>\n"
				+ "CC: \"Darren.vuzzo@myfloridalicence.com\" <Darren.vuzzo@myfloridalicence.com>,\n"
				+ "\t\"Rentfrow, Sandra\" <Sandra.Rentfrow@myfloridalicense.com>, Don Ford\n"
				+ "\t<dford@neptune-beach.com>, \"Taylor, Rick\" <RATaylor@coj.net>,\n"
				+ "\t\"JDouglas@coj.com\" <JDouglas@coj.com>, \"hwhite@sjcfl.us\" <hwhite@sjcfl.us>,\n"
				+ "\t\"building@jaxbchfl.net\" <building@jaxbchfl.net>, Jessica Bazanos\n"
				+ "\t<jessica.bazanos@gmail.com>\n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Topic: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Index: AQHRkNlJKh6lDOIrV0G4sEGvouPLo5+EwThA\n"
				+ "Date: Mon, 11 Apr 2016 13:08:46 +0000\n"
				+ "Message-ID: <73ade2cb375f4f39b9e210a57d087900@ABMAIL13.ci.atlantic-beach.fl.us>\n"
				+ "References: <CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "In-Reply-To: <CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "Accept-Language: en-US\n"
				+ "Content-Language: en-US\n"
				+ "X-MS-Has-Attach:\n"
				+ "X-MS-TNEF-Correlator:\n"
				+ "x-ms-exchange-transport-fromentityheader: Hosted\n"
				+ "x-originating-ip: [10.10.10.117]\n"
				+ "Content-Type: multipart/alternative;\n"
				+ "\tboundary=\"_000_73ade2cb375f4f39b9e210a57d087900ABMAIL13ciatlanticbeach_\"\n"
				+ "MIME-Version: 1.0\n");
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNull();
		assertThat(msg.getBodyRTF()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("Delivery is delayed to these recipients or groups:\n"
				+ "\n"
				+ "Darren.vuzzo@myfloridalicence.com (Darren.vuzzo@myfloridalicence.com)\n"
				+ "<mailto:Darren.vuzzo@myfloridalicence.com> \n"
				+ "\n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "\n"
				+ "This message hasn't been delivered yet. Delivery will continue to be\n"
				+ "attempted.\n"
				+ "\n"
				+ "The server will keep trying to deliver this message for the next 1 days,\n"
				+ "19 hours and 57 minutes. You'll be notified if the message can't be\n"
				+ "delivered by that time.\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "Diagnostic information for administrators:\n"
				+ "\n"
				+ "Generating server: ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Receiving server: myfloridalicence.com (208.73.211.178)\n"
				+ "\n"
				+ "\n"
				+ "Darren.vuzzo@myfloridalicence.com\n"
				+ "Remote Server at myfloridalicence.com (208.73.211.178) returned '400\n"
				+ "4.4.7 Message delayed'\n"
				+ "4/11/2016 4:58:21 PM - Remote Server at myfloridalicence.com\n"
				+ "(208.73.211.178) returned '441 4.4.1 Error encountered while\n"
				+ "communicating with primary target IP address: \"Failed to connect.\n"
				+ "Winsock error code: 10060, Win32 error code: 10060.\" Attempted failover\n"
				+ "to alternate host, but that did not succeed. Either there are no\n"
				+ "alternate hosts, or delivery failed to all alternate hosts. The last\n"
				+ "endpoint attempted was 208.73.211.178:25'\n"
				+ "\n"
				+ "Original message headers:\n"
				+ "\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) by\n"
				+ " ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) with Microsoft SMTP\n"
				+ "Server\n"
				+ " (TLS) id 15.0.1076.9; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "([fe80::3d58:a9bc:50f6:e563])\n"
				+ " by ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563%17])\n"
				+ "with\n"
				+ " mapi id 15.00.1076.000; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "From: \"Arlington, Daniel\" <darlington@coab.us>\n"
				+ "To: ERICKSON ENERGY <ericksonenergy@gmail.com>, \"Reagan, Raina\"\n"
				+ "\t<Raina.Reagan@myfloridalicense.com>\n"
				+ "CC: \"Darren.vuzzo@myfloridalicence.com\"\n"
				+ "<Darren.vuzzo@myfloridalicence.com>,\n"
				+ "\t\"Rentfrow, Sandra\" <Sandra.Rentfrow@myfloridalicense.com>, Don\n"
				+ "Ford\n"
				+ "\t<dford@neptune-beach.com>, \"Taylor, Rick\" <RATaylor@coj.net>,\n"
				+ "\t\"JDouglas@coj.com\" <JDouglas@coj.com>, \"hwhite@sjcfl.us\"\n"
				+ "<hwhite@sjcfl.us>,\n"
				+ "\t\"building@jaxbchfl.net\" <building@jaxbchfl.net>, Jessica Bazanos\n"
				+ "\t<jessica.bazanos@gmail.com>\n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Topic: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Index: AQHRkNlJKh6lDOIrV0G4sEGvouPLo5+EwThA\n"
				+ "Date: Mon, 11 Apr 2016 13:08:46 +0000\n"
				+ "Message-ID:\n"
				+ "<73ade2cb375f4f39b9e210a57d087900@ABMAIL13.ci.atlantic-beach.fl.us>\n"
				+ "References:\n"
				+ "<CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "In-Reply-To:\n"
				+ "<CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "Accept-Language: en-US\n"
				+ "Content-Language: en-US\n"
				+ "X-MS-Has-Attach:\n"
				+ "X-MS-TNEF-Correlator:\n"
				+ "x-ms-exchange-transport-fromentityheader: Hosted\n"
				+ "x-originating-ip: [10.10.10.117]\n"
				+ "Content-Type: multipart/alternative;\n"
				+ "\t\n"
				+ "boundary=\"_000_73ade2cb375f4f39b9e210a57d087900ABMAIL13ciatlanticbeach_\"\n"
				+ "MIME-Version: 1.0\n"
				+ "\n");
	}

	@Test
	public void testEmbeddedImage()
			throws IOException {
		Message msg = parseMsgFile("embedded image.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("Paliarik, Martin");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail("mpaliarik@mdlz.com");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("email test");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("Paliarik, Martin");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("mpaliarik@mdlz.com");
		List<Attachment> attachments = msg.getAttachments();
		assertThat(attachments).hasSize(1);
		assertAttachmentMetadata(attachments.get(0), "image/png", ".png", "image001.png");
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNull();
		assertThat(msg.getBodyRTF()).contains("cid:image001.png");
		assertThat(normalizeText(msg.getBodyText())).contains("This should pass into the kayako\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Martin Paliarik \n"
				+ "Compliance Management Administrator | Procurement Services\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Kraft Foods Europe Procurement GmbH – organizačná zložka | Racianska 44\n"
				+ "| 814 99  Bratislava | Slovakia \n"
				+ "\n"
				+ "Phone: +421 2 494 01 592 | mpaliarik@mdlz.com\n"
				+ "<mailto:mpaliarik@mdlz.com>  | www.mondelezinternational.com\n"
				+ "<http://www.mondelezinternational.com/> \n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "We are a member of the Mondelēz International family of companies from 2\n"
				+ "October 2012, \n"
				+ "but we will still use the name Kraft Foods until our legal entity\n"
				+ "changes in April 2013.\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Description: Description: Description: Description: Description:\n"
				+ "Description: Description: Q:\\062 Kraft Foods C&G Affairs Mondelez Launch\n"
				+ "2012-08\\E-Mail Signatur\\Bilder\\mdlz_rgb_logo_full_e-mail.png\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "Click here\n"
				+ "<mailto:EBSC-Feedback@mdlz.com?subject=Your%20Opinion%20Counts!>  to\n"
				+ "provide a feedback in English about our services\n"
				+ "\n"
				+ " \n"
				+ "\n"
				+ "The Information contained in this e-mail, and any files transmitted with\n"
				+ "it, is confidential and may be legally privileged.  It is intended\n"
				+ "solely for the addressee.  If you are not the intended recipient, please\n"
				+ "return the message by replying to it and then delete the message from\n"
				+ "your computer.  Any disclosure, copying, distribution or action taken in\n"
				+ "reliance on its contents is prohibited and may be unlawful.\n"
				+ "\n"
				+ " \n"
				+ "\n");
	}

	@Test
	public void testMsgForwardDraftWithBothAttachmentsAndEmbeddedImage()
			throws IOException {
		Message msg = parseMsgFile("forward with attachments and embedded images.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName(null);
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail(null);
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("FW: Delivery delayed:RE: Bosco Fraud Cases [ 2 of 8]");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName(null);
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail(null);
		List<Attachment> attachments = msg.getAttachments();
		assertThat(attachments).hasSize(4);
		Attachment attachment1 = attachments.get(0);
		Attachment attachment2 = attachments.get(3);
		assertAttachmentMetadata(attachment1, "message/delivery-status", "", "");
		assertAttachmentMetadata(attachments.get(1), "image/png", ".png", "image001.png");
		assertAttachmentMetadata(attachments.get(2), "image/png", ".png", "image002.png");
		assertAttachmentMetadata(attachment2, "text/rfc822-headers", "", "");
		assertThat(attachment1).isOfAnyClassIn(FileAttachment.class);
		assertThat(attachment2).isOfAnyClassIn(FileAttachment.class);
		String attachmentContent1 = normalizeText(new String(((FileAttachment) attachment1).getData(), UTF_8));
		String attachmentContent2 = normalizeText(new String(((FileAttachment) attachment2).getData(), UTF_8));
		assertThat(attachmentContent1).isEqualTo("Reporting-MTA: dns;ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Received-From-MTA: dns;ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Arrival-Date: Mon, 11 Apr 2016 13:08:47 +0000\n"
				+ "\n"
				+ "Final-Recipient: rfc822;Darren.vuzzo@myfloridalicence.com\n"
				+ "Action: delayed\n"
				+ "Status: 4.4.7\n"
				+ "Diagnostic-Code: smtp;400 4.4.7 Message delayed\n"
				+ "Will-Retry-Until: Wed, 13 Apr 2016 09:08:47 -0400\n"
				+ "X-Display-Name: Darren.vuzzo@myfloridalicence.com\n");
		assertThat(attachmentContent2).isEqualTo("Received: from ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) by\n"
				+ " ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) with Microsoft SMTP Server\n"
				+ " (TLS) id 15.0.1076.9; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563])\n"
				+ " by ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563%17]) with\n"
				+ " mapi id 15.00.1076.000; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "From: \"Arlington, Daniel\" <darlington@coab.us>\n"
				+ "To: ERICKSON ENERGY <ericksonenergy@gmail.com>, \"Reagan, Raina\"\n"
				+ "\t<Raina.Reagan@myfloridalicense.com>\n"
				+ "CC: \"Darren.vuzzo@myfloridalicence.com\" <Darren.vuzzo@myfloridalicence.com>,\n"
				+ "\t\"Rentfrow, Sandra\" <Sandra.Rentfrow@myfloridalicense.com>, Don Ford\n"
				+ "\t<dford@neptune-beach.com>, \"Taylor, Rick\" <RATaylor@coj.net>,\n"
				+ "\t\"JDouglas@coj.com\" <JDouglas@coj.com>, \"hwhite@sjcfl.us\" <hwhite@sjcfl.us>,\n"
				+ "\t\"building@jaxbchfl.net\" <building@jaxbchfl.net>, Jessica Bazanos\n"
				+ "\t<jessica.bazanos@gmail.com>\n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Topic: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Index: AQHRkNlJKh6lDOIrV0G4sEGvouPLo5+EwThA\n"
				+ "Date: Mon, 11 Apr 2016 13:08:46 +0000\n"
				+ "Message-ID: <73ade2cb375f4f39b9e210a57d087900@ABMAIL13.ci.atlantic-beach.fl.us>\n"
				+ "References: <CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "In-Reply-To: <CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com>\n"
				+ "Accept-Language: en-US\n"
				+ "Content-Language: en-US\n"
				+ "X-MS-Has-Attach:\n"
				+ "X-MS-TNEF-Correlator:\n"
				+ "x-ms-exchange-transport-fromentityheader: Hosted\n"
				+ "x-originating-ip: [10.10.10.117]\n"
				+ "Content-Type: multipart/alternative;\n"
				+ "\tboundary=\"_000_73ade2cb375f4f39b9e210a57d087900ABMAIL13ciatlanticbeach_\"\n"
				+ "MIME-Version: 1.0\n");
		assertThat(msg.getBodyRTF()).contains("cid:image001.png");
		assertThat(msg.getBodyRTF()).contains("cid:image002.png");
		assertThat(msg.getConvertedBodyHTML()).contains("cid:image001.png");
		assertThat(msg.getConvertedBodyHTML()).contains("cid:image002.png");
		assertThat(msg.getBodyText()).isNotEmpty();
		assertThat(msg.getBodyHTML()).isNotEmpty();
		assertThat(normalizeText(msg.getBodyText())).isEqualTo(" \n"
				+ " \n"
				+ "Van: Microsoft Outlook\n"
				+ "[mailto:MicrosoftExchange329e71ec88ae4615bbc36ab6ce41109e@coab.us] \n"
				+ "Verzonden: 11 April 2016 19:11\n"
				+ "Aan: darlington@coab.us\n"
				+ "Onderwerp: Delivery delayed:RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ " \n"
				+ "Delivery is delayed to these recipients or groups:\n"
				+ "Darren.vuzzo@myfloridalicence.com (Darren.vuzzo@myfloridalicence.com)\n"
				+ "<mailto:Darren.vuzzo@myfloridalicence.com> \n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "This message hasn't been delivered yet. Delivery will continue to be\n"
				+ "attempted.\n"
				+ "The server will keep trying to deliver this message for the next 1\n"
				+ "days, 19 hours and 57 minutes. You'll be notified if the message can't\n"
				+ "be delivered by that time.\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "Diagnostic information for administrators:\n"
				+ "Generating server: ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "Receiving server: myfloridalicence.com (208.73.211.178)\n"
				+ "Darren.vuzzo@myfloridalicence.com\n"
				+ "<mailto:Darren.vuzzo@myfloridalicence.com> \n"
				+ "Remote Server at myfloridalicence.com (208.73.211.178) returned '400\n"
				+ "4.4.7 Message delayed'\n"
				+ "4/11/2016 4:58:21 PM - Remote Server at myfloridalicence.com\n"
				+ "(208.73.211.178) returned '441 4.4.1 Error encountered while\n"
				+ "communicating with primary target IP address: \"Failed to connect.\n"
				+ "Winsock error code: 10060, Win32 error code: 10060.\" Attempted failover\n"
				+ "to alternate host, but that did not succeed. Either there are no\n"
				+ "alternate hosts, or delivery failed to all alternate hosts. The last\n"
				+ "endpoint attempted was 208.73.211.178:25'\n"
				+ "Original message headers:\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) by\n"
				+ " ABMAIL13.ci.atlantic-beach.fl.us (10.10.10.21) with Microsoft SMTP\n"
				+ "Server\n"
				+ " (TLS) id 15.0.1076.9; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "Received: from ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "([fe80::3d58:a9bc:50f6:e563])\n"
				+ " by ABMAIL13.ci.atlantic-beach.fl.us ([fe80::3d58:a9bc:50f6:e563%17])\n"
				+ "with\n"
				+ " mapi id 15.00.1076.000; Mon, 11 Apr 2016 09:08:47 -0400\n"
				+ "From: \"Arlington, Daniel\" <darlington@coab.us\n"
				+ "<mailto:darlington@coab.us> >\n"
				+ "To: ERICKSON ENERGY <ericksonenergy@gmail.com\n"
				+ "<mailto:ericksonenergy@gmail.com> >, \"Reagan, Raina\"\n"
				+ "        <Raina.Reagan@myfloridalicense.com\n"
				+ "<mailto:Raina.Reagan@myfloridalicense.com> >\n"
				+ "CC: \"Darren.vuzzo@myfloridalicence.com\n"
				+ "<mailto:Darren.vuzzo@myfloridalicence.com> \"\n"
				+ "<Darren.vuzzo@myfloridalicence.com\n"
				+ "<mailto:Darren.vuzzo@myfloridalicence.com> >,\n"
				+ "        \"Rentfrow, Sandra\" <Sandra.Rentfrow@myfloridalicense.com\n"
				+ "<mailto:Sandra.Rentfrow@myfloridalicense.com> >, Don Ford\n"
				+ "        <dford@neptune-beach.com <mailto:dford@neptune-beach.com> >,\n"
				+ "\"Taylor, Rick\" <RATaylor@coj.net <mailto:RATaylor@coj.net> >,\n"
				+ "        \"JDouglas@coj.com <mailto:JDouglas@coj.com> \" <JDouglas@coj.com\n"
				+ "<mailto:JDouglas@coj.com> >, \"hwhite@sjcfl.us <mailto:hwhite@sjcfl.us> \"\n"
				+ "<hwhite@sjcfl.us <mailto:hwhite@sjcfl.us> >,\n"
				+ "        \"building@jaxbchfl.net <mailto:building@jaxbchfl.net> \"\n"
				+ "<building@jaxbchfl.net <mailto:building@jaxbchfl.net> >, Jessica Bazanos\n"
				+ "        <jessica.bazanos@gmail.com <mailto:jessica.bazanos@gmail.com> >\n"
				+ "Subject: RE: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Topic: Bosco Fraud Cases [ 2 of 8]\n"
				+ "Thread-Index: AQHRkNlJKh6lDOIrV0G4sEGvouPLo5+EwThA\n"
				+ "Date: Mon, 11 Apr 2016 13:08:46 +0000\n"
				+ "Message-ID:\n"
				+ "<73ade2cb375f4f39b9e210a57d087900@ABMAIL13.ci.atlantic-beach.fl.us\n"
				+ "<mailto:73ade2cb375f4f39b9e210a57d087900@ABMAIL13.ci.atlantic-beach.fl.u\n"
				+ "s> >\n"
				+ "References:\n"
				+ "<CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com\n"
				+ "<mailto:CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.c\n"
				+ "om> >\n"
				+ "In-Reply-To:\n"
				+ "<CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.com\n"
				+ "<mailto:CAC+pJabatY3d=2QZJV3c62H0k=G+bySP5fjEMM=hU9YDZM2RvA@mail.gmail.c\n"
				+ "om> >\n"
				+ "Accept-Language: en-US\n"
				+ "Content-Language: en-US\n"
				+ "X-MS-Has-Attach:\n"
				+ "X-MS-TNEF-Correlator:\n"
				+ "x-ms-exchange-transport-fromentityheader: Hosted\n"
				+ "x-originating-ip: [10.10.10.117]\n"
				+ "Content-Type: multipart/alternative;\n"
				+ "\n"
				+ "boundary=\"_000_73ade2cb375f4f39b9e210a57d087900ABMAIL13ciatlanticbeach_\"\n"
				+ "MIME-Version: 1.0\n");
	}

	@Test
	public void testHtmlTestWithReplyToAndAttachmentsPlusEmbeddedImage()
			throws IOException {
		Message msg = parseMsgFile("HTML mail with replyto and attachment and embedded image.msg");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromName("lollypop");
		// Google SMTP overrode this, Outlook recognized it as: Benny Bottema <b.bottema@gmail.com>; on behalf of; lollypop <b.bottema@projectnibble.org>
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasFromEmail("b.bottema@projectnibble.org");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasSubject("hey");
		// Outlook overrode this when saving the .msg to match the mail account
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToName("Bottema, Benny");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasToEmail("benny.bottema@aegon.nl");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasReplyToName("lollypop-replyto");
		org.simplejavamail.outlookmessageparser.MessageAssert.assertThat(msg).hasReplyToEmail("lo.pop.replyto@somemail.com");
		assertThat(normalizeText(msg.getBodyText())).isEqualTo("We should meet up!\n");
		// Outlook overrode this value too OR converted the original HTML to RTF, from which MsgParser derived this HTML
		assertThat(normalizeText(msg.getConvertedBodyHTML())).contains(
				"<html><body style=\"font-family:'Courier',monospace;font-size:10pt;\">   <br/>      <br/> <b>   We should meet up! <br/>  </b>   <br/>  <img src=\"cid:thumbsup\"> <br/> ");
		// the RTF was probably created by Outlook based on the HTML when the message was saved
		assertThat(msg.getBodyRTF()).isNotEmpty();
		List<Attachment> attachments = msg.getAttachments();
		assertThat(attachments).hasSize(3);
		Attachment attachment1 = attachments.get(0);
		Attachment attachment2 = attachments.get(1);
		Attachment embeddedImg = attachments.get(2);
		// Outlook overrode dresscode.txt, presumably because it was more than 8 character long??
		assertAttachmentMetadata(attachment1, "text/plain", ".txt", "dressc~1.txt");
		assertAttachmentMetadata(attachment2, "text/plain", ".txt", "location.txt");
		assertAttachmentMetadata(embeddedImg, "image/png", "", "thumbsup");
		String attachmentContent1 = normalizeText(new String(((FileAttachment) attachment1).getData(), UTF_8));
		String attachmentContent2 = normalizeText(new String(((FileAttachment) attachment2).getData(), UTF_8));
		assertThat(attachmentContent1).isEqualTo("Black Tie Optional");
		assertThat(attachmentContent2).isEqualTo("On the moon!");
	}

	private void assertAttachmentMetadata(Attachment embeddedImg, String mimeType, String fileExt, String filename) {
		assertThat(embeddedImg).isOfAnyClassIn(FileAttachment.class);
		assertThat(((FileAttachment) embeddedImg).getMimeTag()).isEqualTo(mimeType);
		assertThat(((FileAttachment) embeddedImg).getExtension()).isEqualTo(fileExt);
		assertThat(((FileAttachment) embeddedImg).getFilename()).isEqualTo(filename);
	}

	private static RecipientEntry createRecipient(String toName, String toEmail) {
		RecipientEntry recipient = new RecipientEntry();
		recipient.setToName(toName);
		recipient.setToEmail(toEmail);
		return recipient;
	}

	private static String normalizeText(String text) {
		return text.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
	}

	private static Message parseMsgFile(String msgPath)
			throws IOException {
		InputStream resourceAsStream = MsgParser.class.getClassLoader().getResourceAsStream(msgPath);
		return new MsgParser().parseMsg(resourceAsStream);
	}
}