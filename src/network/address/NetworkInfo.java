/*******************************************************************************
 * Copyright Yifan Ruan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package network.address;

import java.io.Serializable;

/**
 * Combination of user public and private endpoints
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class NetworkInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private Endpoint privateEndpoint;
	private Endpoint publicEndpoint;
	
	public NetworkInfo(String userId, Endpoint privateEndpoint,
			Endpoint publicEndpoint) {
		this.userId = userId;
		this.privateEndpoint = privateEndpoint;
		this.publicEndpoint = publicEndpoint;
	}

	public String getUserId() {
		return userId;
	}

	public Endpoint getPrivateEndpoint() {
		return privateEndpoint;
	}

	public Endpoint getPublicEndpoint() {
		return publicEndpoint;
	}
	
}
