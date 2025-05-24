import { Button } from "../ui/button";

export const Header = () => (
	<header className="h-16 bg-blue-500 border-b flex items-center justify-between px-6">
		<div className="font-bold text-xl">見積WEB</div>
		<div className="flex items-center gap-4">
			<span>123456 営業部</span>
			<span>〇〇太郎</span>
			<Button variant="super" size="sm">
				ログアウト
			</Button>
		</div>
	</header>
);
