import { Loader2 } from 'lucide-react'
import { EstimateTable } from '../components/estimate/EstimateTable'
import { useEstimates } from '../hooks/useEstimates'
import { Button } from '../components/ui/button'
import { EstimateModal } from '../components/estimate/EstimateModal'
import { useState } from 'react'
import type { Estimate } from '../types/Estimate'

export const Estimates = () => {
  const { estimates, error, isLoading, mutate } = useEstimates()
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Estimate | null>(null);

  const handleAdd = () => {
    setEditTarget(null);
    setDialogOpen(true);
  };

  const handleEdit = (estimate: Estimate) => {
    setEditTarget(estimate);
    setDialogOpen(true);
  };
  return (
    <div className="flex flex-col items-center justify-center h-full min-h-[60vh]">
      <Button onClick={handleAdd} className="mb-4">+ 追加</Button>
      <EstimateModal
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        defaultValue={editTarget ?? undefined}
        onSubmitted={mutate}
      />
      {isLoading ? (
        <div className="p-8 text-gray-500">
          <Loader2 className="h-5 w-5 animate-spin inline-block mr-2" />
          Loading...
        </div>
      ) : error ? (
        <div className="text-red-500">Failed...</div>
      ) : (
        <EstimateTable estimates={estimates} onEdit={handleEdit} onDeleted={mutate}/>
      )}
    </div>
  )
}
