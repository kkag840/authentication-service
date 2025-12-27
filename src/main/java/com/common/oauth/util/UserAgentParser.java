package com.common.oauth.util;

import com.common.oauth.model.UserAgentDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentParser {
	private String userAgentString;
	private String browserName;
	private String browserVersion;
	private String browserOperatingSystem;
	private List<UserAgentDetails> parsedBrowsers = new ArrayList<>();
	private static Pattern pattern = Pattern
			.compile("([^/\\s]*)(/([^\\s]*))?(\\s*\\[[a-zA-Z][a-zA-Z]\\])?\\s*(\\((([^()]|(\\([^()]*\\)))*)\\))?\\s*");

	public UserAgentParser(String userAgentString) {
		this.userAgentString = userAgentString;

		String nextBrowserName;
		String nextBrowserVersion;
		String nextBrowserComments;
		for (Matcher matcher = pattern.matcher(this.userAgentString); matcher.find(); this.parsedBrowsers
				.add(new UserAgentDetails(nextBrowserName, nextBrowserVersion, nextBrowserComments))) {
			nextBrowserName = matcher.group(1);
			nextBrowserVersion = matcher.group(3);
			nextBrowserComments = null;
			if (matcher.groupCount() >= 6) {
				nextBrowserComments = matcher.group(6);
			}
		}

		if (this.parsedBrowsers.size() > 0) {
			this.processBrowserDetails();
		} else {
			throw new UserAgentParseException("Unable to parse user agent string: " + userAgentString);
		}
	}

	public boolean isMobile() {
		return this.browserOperatingSystem != null && (this.browserOperatingSystem.equalsIgnoreCase("mandroid")
				|| this.browserOperatingSystem.equalsIgnoreCase("mios"));
	}

	private void processBrowserDetails() {
		String[] browserNameAndVersion = this.extractBrowserNameAndVersion();
		this.browserName = browserNameAndVersion[0];
		this.browserVersion = browserNameAndVersion[1];
		this.browserOperatingSystem = this
				.extractOperatingSystem(((UserAgentDetails) this.parsedBrowsers.get(0)).getBrowserComments());
	}

	private String[] extractBrowserNameAndVersion() {
		String[] knownBrowsers = new String[] { "firefox", "netscape", "chrome", "safari", "camino", "mosaic", "opera",
				"galeon" };
		Iterator<UserAgentDetails> var2 = this.parsedBrowsers.iterator();

		int firstSpace;
		int firstSlash;
		while (var2.hasNext()) {
			UserAgentDetails nextBrowser = (UserAgentDetails) var2.next();
			String[] var4 = knownBrowsers;
			firstSpace = knownBrowsers.length;

			for (firstSlash = 0; firstSlash < firstSpace; ++firstSlash) {
				String nextKnown = var4[firstSlash];
				if (nextBrowser.getBrowserName().toLowerCase().startsWith(nextKnown)) {
					return new String[] { nextBrowser.getBrowserName(), nextBrowser.getBrowserVersion() };
				}
			}
		}

		UserAgentDetails firstAgent = (UserAgentDetails) this.parsedBrowsers.get(0);
		if (!firstAgent.getBrowserName().toLowerCase().startsWith("mozilla")) {
			return new String[] { firstAgent.getBrowserName(), firstAgent.getBrowserVersion() };
		} else {
			if (firstAgent.getBrowserComments() != null) {
				String[] comments = firstAgent.getBrowserComments().split(";");
				if (comments.length > 2 && comments[0].toLowerCase().startsWith("compatible")) {
					String realBrowserWithVersion = comments[1].trim();
					firstSpace = realBrowserWithVersion.indexOf(32);
					firstSlash = realBrowserWithVersion.indexOf(47);
					if ((firstSlash <= -1 || firstSpace <= -1) && (firstSlash <= -1 || firstSpace != -1)) {
						if (firstSpace > -1) {
							return new String[] { realBrowserWithVersion.substring(0, firstSpace),
									realBrowserWithVersion.substring(firstSpace + 1) };
						}

						return new String[] { realBrowserWithVersion, null };
					}

					return new String[] { realBrowserWithVersion.substring(0, firstSlash),
							realBrowserWithVersion.substring(firstSlash + 1) };
				}
			}

			if ((double) (new Float(firstAgent.getBrowserVersion())).floatValue() < 5.0D) {
				return new String[] { "Netscape", firstAgent.getBrowserVersion() };
			} else {
				return new String[] { "Mozilla", firstAgent.getBrowserComments().split(";")[0].trim() };
			}
		}
	}

	private String extractOperatingSystem(String comments) {
		if (comments == null) {
			return null;
		} else {
			String[] knownOS = new String[] { "win", "linux", "mandroid", "mios", "mac", "freebsd", "netbsd", "openbsd",
					"sunos", "amiga", "beos", "irix", "iphone", "warp", "iphone" };
			List<String> osDetails = new ArrayList<>();
			String[] parts = comments.split(";");
			String[] var5 = parts;
			int var6 = parts.length;

			for (int var7 = 0; var7 < var6; ++var7) {
				String comment = var5[var7];
				String lowerComment = comment.toLowerCase().trim();
				String[] var10 = knownOS;
				int var11 = knownOS.length;

				for (int var12 = 0; var12 < var11; ++var12) {
					String os = var10[var12];
					if (lowerComment.startsWith(os)) {
						osDetails.add(comment.trim());
					}
				}
			}

			switch (osDetails.size()) {
			case 0:
				return null;
			case 1:
				return (String) osDetails.get(0);
			default:
				return (String) osDetails.get(0);
			}
		}
	}

	public String getBrowserName() {
		return this.browserName;
	}

	public String getBrowserVersion() {
		return this.browserVersion;
	}

	public String getBrowserOperatingSystem() {
		return this.browserOperatingSystem;
	}
}
