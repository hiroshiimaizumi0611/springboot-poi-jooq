import { api } from "../lib/axios";

export interface LoginResponse {
	accessToken: string;
	refreshToken: string;
}

export async function login(username: string, password: string): Promise<void> {
	const res = await api.post<LoginResponse>("/api/login", {
		username,
		password,
	});
	localStorage.setItem("accessToken", res.data.accessToken);
	localStorage.setItem("refreshToken", res.data.refreshToken);
}

export async function logout(): Promise<void> {
	const refreshToken = localStorage.getItem("refreshToken");
	const accessToken = localStorage.getItem("accessToken");
	await api.post(
		"/api/logout",
		{ refreshToken },
		{
			headers: {
				Authorization: accessToken ? `Bearer ${accessToken}` : undefined,
			},
		},
	);
	localStorage.removeItem("accessToken");
	localStorage.removeItem("refreshToken");
	window.location.href = "/login";
}
