function Set-NewFileByName
{
    param(
        [String]$root,
        [String]$find,
        [String]$targetToReplace
    )
    $root = [System.IO.Path]::GetFullPath($root)
    $targetToReplace = [System.IO.Path]::GetFullPath($targetToReplace)
    Write-Host "Replacing $find in $root to $targetToReplace"
    Get-ChildItem $root -Recurse -include $find | % {
        $destPath = $_.fullName
        Write-Host "Target: $destPath"
        $isDir = $_ -is [System.IO.DirectoryInfo]
        Remove-Item -Recurse $destPath |Out-Null
        if ($isDir)
        {
            robocopy /mir $targetToReplace $destPath |Out-Null
        }
        else
        {
            Copy-Item -Path $targetToReplace -Destination $destPath
        }
    }
}

Export-ModuleMember -Function Set-NewFileByName
