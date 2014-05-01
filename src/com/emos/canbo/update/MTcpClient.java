package com.emos.canbo.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;

import android.util.Log;

import com.emos.canbo.tools.MD5Util;

public class MTcpClient implements ITcpRequest{
	public final static int RD_BUFFER_SIZE = 8192;
	
	String remote_ip = "";
	int remote_port = -1;
	
	Socket mSocket;
	OutputStream os = null;
	InputStream is = null;
	
	public MTcpClient() {
		super();
	}

	@Override
	public boolean open(String ip, int port) {
		this.remote_ip = ip;
		this.remote_port = port;
		return false;
	}

	@Override
	public boolean connect() throws UnknownHostException, IOException {
		mSocket = new Socket(remote_ip, remote_port);
		if(mSocket.isConnected()){
			os = mSocket.getOutputStream();
			is = mSocket.getInputStream();
			return true;
		}
		return false;
	}

	@Override
	public String requestByText(String req) throws IOException, MTcpClientException {
		sendReq(req);
		int n = 0, count = 0;
		byte[] buffer = new byte[RD_BUFFER_SIZE];
		while(0 < (n = is.read(buffer,count,RD_BUFFER_SIZE-count))){
			count += n;
		}
		String res = new String(buffer,0,count,"UTF-8");
		return res;
	}

	@Override
	public boolean requestByStream(String req, File file, boolean override, int totalSize) throws MTcpClientException, SocketException,IOException{
		//send request.
		sendReq(req);
		//check file.
		if(file.exists()){
			if(!override){
				throw new MTcpClientException("file is already exist.", MTcpClientException.FILE_EXIST);
			}else{
				file.delete();
			}
		}
		FileOutputStream fos = new FileOutputStream(file);
		//get result.
		MTcpClient.this.startDownloadListener.startDownload();	//start download interface
		byte[] buffer = new byte[RD_BUFFER_SIZE];
		int n=0,count=0, progress = 0;
		while(0 < (n = is.read(buffer))){
			fos.write(buffer,0,n);
			count += n;
			if(totalSize > 0){
				progress = (int)(((double)count)/totalSize * 100);
				this.progressListener.report(progress);
			}
		}
		System.out.println("download file(B): " + count);
		fos.close();
		return true;
	}

	@Override
	public boolean requestByStreamWithCheck(String req, File file, int totalSize,
			String checkSum) throws IOException, MTcpClientException, SocketException {
		boolean rtvl = false;
		rtvl = requestByStream(req, file, true, totalSize);
		if(false == rtvl){
			return false;
		}
		// check MD5.
		String sum = MD5Util.getFileMD5(file);
		sum = sum.toUpperCase(Locale.US);
		System.out.println(sum);
		rtvl = sum.equals(checkSum.toUpperCase(Locale.US));
		if(false == rtvl){
			throw new MTcpClientException("md5 checksum failed.", MTcpClientException.FILE_VERIFY_FAILED);
		}
		System.out.println("check MD5 ok!!");
		return true;
	}
	
	private boolean sendReq(String req) throws IOException, MTcpClientException{
		//to bytes
		req += "\n";
		byte[] bytes = req.getBytes();
		if(is != null){
			os.write(bytes, 0, bytes.length); //send
		}else{
			throw new MTcpClientException("sending in unavailable socket", MTcpClientException.SOCKET_INPUT_ERR);
		}
		os.flush();
		return true;
	}

	@Override
	public void close() throws IOException {
		if(is!=null)
			is.close();
		if(os!=null)
			os.close();
		if(mSocket!=null)
			mSocket.close();
	}
	
	public interface IProgressListener{
		void report(int progress);
	}	
	private IProgressListener progressListener;
	public void setProgressListener(IProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	public interface IStartDownloadListener{
		void startDownload();
	}	
	private IStartDownloadListener startDownloadListener;
	public void setStartDownloadListener(IStartDownloadListener startDownloadListener) {
		this.startDownloadListener = startDownloadListener;
	}
}
