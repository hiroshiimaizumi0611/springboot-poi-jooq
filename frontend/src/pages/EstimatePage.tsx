import { downloadEstimatesExcel } from "../api/estimate";
import { Button } from "../components/ui/button";

export const EstimatePage = () => {
	return (
		<div className="flex items-center justify-center h-full text-2xl text-gray-400">
			<Button
				variant="default"
				className="text-xl font-bold mb-2"
				onClick={downloadEstimatesExcel}
			>
				EstimateDownload
			</Button>
		</div>
	);
};
