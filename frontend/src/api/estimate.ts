import { api } from '../lib/axios'
import { downloadBlob } from '../lib/download'

export async function downloadEstimatesExcel() {
  const response = await api.get('/api/estimates/download', {
    responseType: 'blob',
  })

  const disposition = response.headers['content-disposition']
  const fileName = disposition
    ? disposition.match(/filename="?(.+)"?/)?.[1] || 'estimates.xlsx'
    : 'estimates.xlsx'

  downloadBlob(response.data, fileName)
}
