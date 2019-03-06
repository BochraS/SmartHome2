import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';
import { AccountService } from 'app/core';
import { ProfileConfValueService } from './profile-conf-value.service';

@Component({
    selector: 'jhi-profile-conf-value',
    templateUrl: './profile-conf-value.component.html'
})
export class ProfileConfValueComponent implements OnInit, OnDestroy {
    profileConfValues: IProfileConfValue[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected profileConfValueService: ProfileConfValueService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.profileConfValueService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IProfileConfValue[]>) => (this.profileConfValues = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.profileConfValueService.query().subscribe(
            (res: HttpResponse<IProfileConfValue[]>) => {
                this.profileConfValues = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProfileConfValues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProfileConfValue) {
        return item.id;
    }

    registerChangeInProfileConfValues() {
        this.eventSubscriber = this.eventManager.subscribe('profileConfValueListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
