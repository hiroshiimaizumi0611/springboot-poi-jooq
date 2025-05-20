import { Link } from "react-router-dom";
import AppRoutes from "./routes";

export const App = () => {
	return (
		<div className="p-4">
			<nav className="mb-4">
				<Link to="/" className="mr-4">
					Home
				</Link>
				<Link to="/estimate" className="text-blue-700">
					EstimateDownload
				</Link>
			</nav>
			<AppRoutes />
		</div>
	);
};
