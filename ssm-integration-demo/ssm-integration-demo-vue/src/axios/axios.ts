import axios from "axios";

const instance = axios.create({
	baseURL: "http://localhost:8080",
	timeout: 5000
})

instance.interceptors.request.use(
	(config) => {
		config.headers.set("Accept", "application/json,text/plain,text/html");
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
)

instance.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		// const code = error?.response?.data?.code;
		return Promise.reject(error);
	}
)
export  default instance;