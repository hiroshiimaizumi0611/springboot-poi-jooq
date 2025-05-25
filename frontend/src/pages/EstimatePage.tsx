import { Download } from 'lucide-react'
import { downloadEstimatesExcel } from '../api/estimate'
import { Button } from '../components/ui/button'

export const EstimatePage = () => {
  return (
    <div className="flex items-center justify-center h-full text-2xl text-gray-400">
      <Button
        size="lg"
        className="flex w-auto"
        onClick={downloadEstimatesExcel}
      >
        <Download className="mr-2 h-4 w-4" />
        ESTIMATE DOWNLOAD
      </Button>
    </div>
  )
}
