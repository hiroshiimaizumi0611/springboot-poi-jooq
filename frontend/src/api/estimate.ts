import { api } from '../lib/axios'
import { downloadBlob } from '../lib/download'
import type { Estimate } from '../types/Estimate'

export async function downloadEstimatesExcel(): Promise<void> {
  const response = await api.get('/api/estimates/download', {
    responseType: 'blob',
  })

  const disposition = response.headers['content-disposition']
  const fileName = disposition
    ? disposition.match(/filename="?(.+)"?/)?.[1] || 'estimates.xlsx'
    : 'estimates.xlsx'

  downloadBlob(response.data, fileName)
}

export async function getEstimates(): Promise<Estimate[]> {
  const res = await api.get<Estimate[]>('/api/estimates')
  return res.data
}

export async function addEstimate(estimate: Omit<Estimate, 'id'>) {
  await api.post('/api/estimates', estimate)
}

export async function deleteEstimate(id: string) {
  await api.delete(`/api/estimates/${id}`)
}

export async function updateEstimate(estimate: Estimate) {
  await api.put(`/api/estimates/${estimate.id}`, estimate);
}
