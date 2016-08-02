/*
 * Copyright 2011-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.singledog.monkey.commands;

import org.singledog.monkey.comm.Closeable;
import org.singledog.monkey.comm.GlobalConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Jarred Li
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyHistoryFileNameProvider extends DefaultHistoryFileNameProvider implements Closeable {

	public String getHistoryFileName() {
		return GlobalConstants.historyFileName;
	}

	@Override
	public String getProviderName() {
		return "My history file name provider";
	}

	@Override
	public void close() {
		String key = "server-auth-add";
		if (!GlobalConstants.OS.contains("Windows")) {
			String command = " sed -i -n '/" + key + "/d' " + getHistoryFileName();
			try {
				Runtime.getRuntime().exec(command);
				return;
			} catch (IOException e) {
				if (GlobalConstants.isDebugEnabled) {
					e.printStackTrace();
				}
			}
		}

		BufferedReader reader = null;
		FileOutputStream fileOutputStream = null;
		try {
			reader = new BufferedReader(new FileReader(getHistoryFileName()));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.contains(key))
					continue;
				stringBuilder.append(line).append(GlobalConstants.LINE_SEPARATOR);
			}

			fileOutputStream = new FileOutputStream(getHistoryFileName());
			fileOutputStream.write(stringBuilder.toString().getBytes());
			fileOutputStream.flush();
		} catch (IOException e) {
			if (GlobalConstants.isDebugEnabled) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
			} catch (IOException e) {
				if (GlobalConstants.isDebugEnabled) {
					e.printStackTrace();
				}
			}
		}
	}
}