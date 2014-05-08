package com.emos.canbo.sync;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public interface ITcpRequest {
	boolean open(String ip, int port);
	boolean connect() throws UnknownHostException, IOException;
	String requestByText(String req) throws IOException, MTcpClientException;
	void close() throws IOException;
	boolean requestByStream(String req, File file, boolean override,
			int totalSize) throws MTcpClientException, IOException;
	boolean requestByStreamWithCheck(String req, File file, int totalSize,
			String checkSum) throws IOException, MTcpClientException;
}
