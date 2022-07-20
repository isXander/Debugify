import re
import requests
import sys
import os

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

with open('../PATCHED.md', 'r') as f:
    patched_lines = f.readlines()

bugs = []
start_unpatched = False
pattern = re.compile('\\|.+\\| \\[(MC-\\d+)]')

for line in patched_lines:
    if line.startswith('## '):
        if start_unpatched:
            break
        start_unpatched = line == '## Unpatched in vanilla\n'

    if not start_unpatched:
        continue

    match = re.search(pattern, line)
    if match is None:
        continue
    bugs.append(match.group(1))

count = 0
for bug in bugs:
    response = requests.get(f'https://bugs.mojang.com/rest/api/2/issue/{bug}')
    json_response = response.json()

    fields = json_response['fields']
    resolution_id = fields['resolution']['id'] if 'resolution' in fields and fields['resolution'] is not None else -1

    resolved = resolution_id == 1
    fix_versions = []
    if resolved:
        for version in fields['fixVersions'] or []:
            fix_versions.append(version['name'])

        count += 1

    message = f'{bug}: {"Resolved" if resolved else "OK!"}'
    if resolved:
        if len(fix_versions) > 0:
            message += f' - Fix Versions: {", ".join(fix_versions)}'
        message = '\033[91m' + message + '\033[0m'
    print(message, file=sys.stderr if resolved else sys.stdout)

if count == 0:
    print('\nNothing to report!')
else:
    print(f'\n{count} bug{"s" if count > 1 else ""} need removing!')
    exit(-1)
