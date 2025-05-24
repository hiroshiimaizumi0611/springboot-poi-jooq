import { Link } from "react-router-dom";
import { Avatar } from "../ui/avatar";
import { Button } from "../ui/button";
import { Separator } from "../ui/separator";

export const SideBar = () => (
	<aside className="w-64 bg-white border-r flex-shrink-0 flex flex-col py-6 px-4">
		<div className="mb-8 flex items-center">
			<Avatar className="mr-3" />
			<span className="font-bold text-lg">メニュー</span>
		</div>
		<Separator />
		<nav className="flex-1 mt-8 flex flex-col gap-2">
			<Link to="/" className="mr-4">
				<Button variant="secoundary" className="justify-start w-full">
					ホーム
				</Button>
			</Link>
			<Link to="/estimate" className="mr-4">
				<Button variant="secoundary" className="justify-start w-full">
					ダウンロード
				</Button>
			</Link>
			<Button variant="ghost" className="justify-start w-full">
				見積管理
			</Button>
			<Button variant="ghost" className="justify-start w-full">
				受注一覧
			</Button>
			<Button variant="ghost" className="justify-start w-full">
				会社情報
			</Button>
			{/* 他メニュー... */}
		</nav>
	</aside>
);
