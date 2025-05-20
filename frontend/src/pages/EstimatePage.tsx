import { downloadEstimatesExcel } from '../api/estimate'

export const EstimatePage = () => {
  return (
    <div className="flex flex-col justify-center">
      <button
        type="button"
        onClick={downloadEstimatesExcel}
        className="text-xl font-bold mb-2"
      >
        EstimateDownload
      </button>
    </div>
  )
}
