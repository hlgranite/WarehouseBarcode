/*
 * Created by SharpDevelop.
 * User: yeang-shing.then
 * Date: 9/20/2013
 * Time: 1:44 PM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
using NUnit.Framework;
using System;
using System.IO;
using System.Net;
using System.Text;
using System.Collections;
using System.Reflection;

namespace WarehouseScanner.Test
{
	/// <summary>
	/// Description of FusionTableTest.
	/// </summary>
	[TestFixture]
	public class FusionTableTest
	{
		[Test]
		public void GetFusionTableTest()
		{
			string url = "https://www.googleapis.com/fusiontables/v1/query?sql=SELECT+*+FROM+1hyYCTWWMIXFtnd83UL6G_4ZoTDNJSoUGKwzazuM&key=AIzaSyCY5_CBNvNUlJv5LILyOHMEeRfzdolcM5k";
			HttpWebRequest request = WebRequest.Create(new Uri(url)) as HttpWebRequest;
			using(HttpWebResponse response = request.GetResponse() as HttpWebResponse)
			{
				//Read the cookies into our return variable
                foreach (Cookie cookie in response.Cookies)
                {
                    System.Diagnostics.Debug.WriteLine(cookie.Name + ":" + cookie.Value);
                    //cookies.Add(new Cookie(cookie.Name, cookie.Value, cookie.Path, cookie.Domain));
                }
                
                string output = string.Empty;
                using(Stream stream = response.GetResponseStream())
				{
                	using(StreamReader reader = new StreamReader(stream))
                		output = reader.ReadToEnd();
				}
                System.Diagnostics.Debug.WriteLine(output);
                
                bool actual = output.Contains("fusiontables#sqlresponse");
                Assert.IsTrue(actual);
			}
		}
		private CookieContainer GetClientSecrets()
		{
			
		}
		[Test]
		public void PostFusionTableTest()
		{			
			string url = "https://www.googleapis.com/fusiontables/v1/query";
			string content = string.Format("sql={0}&key={1}",
			                                  "INSERT+INTO+1hyYCTWWMIXFtnd83UL6G_4ZoTDNJSoUGKwzazuM+(code,name)+VALUES+('TE13','Test13')",
			                                  "AIzaSyCY5_CBNvNUlJv5LILyOHMEeRfzdolcM5k");
			byte[] byteArray = Encoding.UTF8.GetBytes(content);
			
			HttpWebRequest request = WebRequest.Create(new Uri(url)) as HttpWebRequest;
			request.Method = "POST";
			request.ContentType = "application/x-www-form-urlencoded";
			request.ContentLength = byteArray.Length;
			request.CookieContainer = GetClientSecrets();// GetGoogleCredential();
			
			Stream dataStream = request.GetRequestStream();
		    dataStream.Write(byteArray, 0, byteArray.Length);
		    dataStream.Close();
		    
		    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
		    Stream stream = response.GetResponseStream();
		    StreamReader reader = new StreamReader(stream);
		    string output = reader.ReadToEnd();
			reader.Close();		    
            bool actual = output.Contains("fusiontables#sqlresponse");
            Assert.IsTrue(actual);
		}
		[Test]
		public void LoginGmailTest()
		{
		    string content = String.Format("Email={0}&Passwd={1}",
			                               "yancyn@gmail.com",
			                               "tackle1by1");
		    byte[] byteArray = Encoding.UTF8.GetBytes(content);
		    
		    HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://accounts.google.com/ServiceLogin");//"http://www.google.com/accounts/ClientLogin");
		    request.ContentLength = byteArray.Length;
		    request.ContentType = "application/x-www-form-urlencoded";
		    request.Method = "POST";
		    
		    Stream dataStream = request.GetRequestStream();
		    dataStream.Write(byteArray, 0, byteArray.Length);
		    dataStream.Close();
		    
		    using(HttpWebResponse response = request.GetResponse() as HttpWebResponse)
			{
		    	Stream stream = response.GetResponseStream();
			    StreamReader reader = new StreamReader(stream);
			    string loginStuff = reader.ReadToEnd();
			    reader.Close();
			    System.Diagnostics.Debug.WriteLine(loginStuff);
			    
				//Read the cookies into our return variable
				System.Diagnostics.Debug.WriteLine("Cookies:");
                foreach (Cookie cookie in response.Cookies)
                {
                    System.Diagnostics.Debug.WriteLine(cookie.Name + ":" + cookie.Value);
                    //cookies.Add(new Cookie(cookie.Name, cookie.Value, cookie.Path, cookie.Domain));
                }
			}
		}
		/// <summary>
		/// Source http://stackoverflow.com/questions/6038985/programmatically-login-to-google-app-engine-c-sharp
		/// </summary>
		[Test]
		public void GetGoogleCredentialTest()
		{
			GetGoogleCredential();
		}
		
		private CookieContainer GetGoogleCredential()
		{
			CookieContainer cookies = new CookieContainer();
			
//			string url = "https://accounts.google.com/ServiceLoginAuth";
//			string postString = string.Format("Email={0}&Passwd={1}", "yancyn@gmail.com", "tackle1by1");
//			HttpWebRequest request = WebRequest.Create(new Uri(url)) as HttpWebRequest;
//			request.Method = "POST";
//			request.ContentType = "application/x-www-form-urlencoded";
//			request.ContentLength = postString.Length;
//			request.CookieContainer = cookies;
//			
//			StreamWriter writer = new StreamWriter(request.GetRequestStream());
//			writer.Write(postString);
//			writer.Close();
//			
//			using(HttpWebResponse response = request.GetResponse() as HttpWebResponse)
//			{
//				//Read the cookies into our return variable
//                foreach (Cookie cookie in response.Cookies)
//                {
//                    System.Diagnostics.Debug.WriteLine(cookie.Name + ":" + cookie.Value);
//                    cookies.Add(new Cookie(cookie.Name, cookie.Value, cookie.Path, cookie.Domain));
//                }
//			}

			string content = String.Format("Email={0}&Passwd={1}&service=ah&accountType=HOSTED_OR_GOOGLE",
			                               "yancyn@gmail.com",
			                               "tackle1by1");		    
		    string url = "http://www.google.com/accounts/ClientLogin?" + content;
			HttpWebRequest request = WebRequest.Create(new Uri(url)) as HttpWebRequest;
			using(HttpWebResponse response = request.GetResponse() as HttpWebResponse)
			{
				Stream stream = response.GetResponseStream();
			    StreamReader reader = new StreamReader(stream);
			    string output = reader.ReadToEnd();
			    reader.Close();
			    System.Diagnostics.Debug.WriteLine(output);
			    
			    System.Diagnostics.Debug.WriteLine("Split:");
			    string[] collection = output.Split(new char[]{'\n'});
			    foreach(String item in collection)
			    {
			    	System.Diagnostics.Debug.WriteLine(item);
			    	string[] pieces = item.Split(new char[]{'='});
			    	if(pieces.Length>1)
			    		cookies.Add(new Cookie(pieces[0],pieces[1],"/","localhost"));
			    }
    
			    // No result: Read the cookies into our return variable
//			    System.Diagnostics.Debug.WriteLine("Cookies:");
//                foreach (Cookie cookie in response.Cookies)
//                {
//                    System.Diagnostics.Debug.WriteLine(cookie.Name + ":" + cookie.Value);
//                    cookies.Add(new Cookie(cookie.Name, cookie.Value, cookie.Path, cookie.Domain));
//                }
			}
			
			// no result
//			System.Diagnostics.Debug.WriteLine("Cookied in request:");
//			CookieContainer container = request.CookieContainer;
//			Hashtable table = (Hashtable) cookies.GetType().InvokeMember("m_domainTable",
//                                                                         BindingFlags.NonPublic |
//                                                                         BindingFlags.GetField |
//                                                                         BindingFlags.Instance,
//                                                                         null,
//                                                                         cookies,
//                                                                         new object[] { });
//            foreach (var key in table.Keys)
//            {
//                foreach (Cookie cookie in cookies.GetCookies(new Uri(string.Format("http://{0}/", key))))
//                {
//                    Console.WriteLine("Name = {0} ; Value = {1} ; Domain = {2}",
//                	                  cookie.Name, cookie.Value, cookie.Domain);
//                }
//            }
			
			return cookies;
		}
	}
}
